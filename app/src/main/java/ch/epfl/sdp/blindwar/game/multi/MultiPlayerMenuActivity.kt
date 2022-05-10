package ch.epfl.sdp.blindwar.game.multi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.MatchDatabase
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.game.multi.model.Match
import ch.epfl.sdp.blindwar.menu.MainMenuActivity
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


/**
 * Activity that lets the user start a multiplayer game
 *
 * @constructor creates a MultiPlayerMenuActivity
 */
class MultiPlayerMenuActivity : AppCompatActivity() {
    private var eloDelta = 200
    private var dialog: AlertDialog? = null
    private var isCanceled = false
    private var listener: ListenerRegistration? = null
    private lateinit var toast: Toast

    companion object {
        private const val LIMIT_MATCH: Long = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiplayer_menu)
        toast = Toast.makeText(applicationContext, "default", Toast.LENGTH_SHORT)
        eloDelta = 200
    }

    /**
     * Starts a multiplayer game played with a friend
     *
     * @param view
     */
    fun friendButton(view: View) {
        assert(view.isEnabled)
        setLinkDialog()
    }

    /**
     * Launch the create match activity
     *
     * @param view
     */
    fun createMatch(view: View) {
        assert(view.isEnabled)
        startActivity(Intent(this, ChoseNumberOfPlayerActivity::class.java))
    }

    /**
     * Cancel
     *
     * @param view
     */
    fun cancel(view: View) {
        assert(view.isEnabled)
        startActivity(Intent(this, MainMenuActivity::class.java))
    }

    /**
     * Starts a multiplayer game played with a random user
     *
     * @param view
     */
    fun randomButton(view: View) {
        setProgressDialog(getString(R.string.multi_wait_matches))
        val user = UserDatabase.getCurrentUser()
        val elo = user?.child("userStatistics/elo")?.value as Int
        val matches = Firebase.firestore.collection("match")
            .whereEqualTo("isPrivate", false)
            .whereLessThan("elo", elo + eloDelta)
            .whereGreaterThan("elo", elo - 200)
            .orderBy("elo", Query.Direction.DESCENDING)
            .limit(LIMIT_MATCH).get()
        if (matches.isSuccessful && !isCanceled) {
            var i = 0
            var match: DocumentReference? = null
            while (match == null && i < LIMIT_MATCH) {
                match =
                    MatchDatabase.connect(
                        matches.result.documents[i].toObject(Match::class.java)!!,
                        user.getValue(User::class.java)!!,
                        Firebase.firestore
                    )
                i++
            }
            if (match == null && !isCanceled) {
                dialog!!.hide()
                toast.setText(getString(R.string.toast_connexion))
                toast.show()
                eloDelta += 100
                randomButton(view)
            } else if (!isCanceled) {
                setListener(match!!)
            }
        } else if (!isCanceled) {
            toast.setText(getString(R.string.toast_connexion_internet))
            toast.show()
            dialog!!.hide()
            listener?.remove()
            randomButton(view)
        }
    }

    /**
     * display progressDialog cancelable for any messages
     *
     * @param message
     */
    private fun setProgressDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setNeutralButton(
            getString(R.string.cancel_btn)
        ) { it, _ -> it.cancel() }
        builder.setOnCancelListener {
            isCanceled = true
            toast.setText(getString(R.string.toast_canceled_connexion))
            toast.show()
        }
        val view = View.inflate(applicationContext, R.layout.fragment_dialog_loading, null)
        builder.setView(view)
        (view.findViewById<TextView>(R.id.textView_multi_loading)).text = message
        dialog = builder.create()
        dialog!!.show()
    }


    /**
     * return null if dynamic link is false, otherwise return the match uid
     *
     * @param uri the dynamic link
     * @return
     */
    private fun parseDynamicLink(uri: String): String? {
        if (uri.substring(0, 29) != "https://blindwar.ch/game?uid=")
            return null
        return uri.trim().substring(29) //https://blindwar.ch/game + ?uid=
    }

    /**
     * find a match with the link on DB which is free
     *
     * @param uid
     */
    private fun connectToDB(uid: String) {
        setProgressDialog("Wait for connexion")
        val user = UserDatabase.getCurrentUser()
        val matchDoc = Firebase.firestore.collection("match").document(uid)
        val match = matchDoc.get()
        if (match.isSuccessful && !isCanceled) {
            val matchObject = match.result.toObject(Match::class.java)!!
            val connect =
                MatchDatabase.connect(
                    matchObject,
                    user?.getValue(User::class.java)!!,
                    Firebase.firestore
                )
            if (connect == null && !isCanceled) {
                toast.setText(getString(R.string.multi_match_full))
                toast.show()
                dialog!!.hide()
            } else if (!isCanceled) {
                setListener(matchDoc)
            }
        } else if (!isCanceled) {
            toast.setText(getString(R.string.multi_match_not_found))
            toast.show()
            dialog!!.hide()
        }
    }

    /**
     * create a dialog which ask for the uid of the match
     *
     */
    private fun setLinkDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        val view = View.inflate(applicationContext, R.layout.fragment_multi_connexion_link, null)
        builder.setView(view)
        builder.setNeutralButton(resources.getText(R.string.cancel_btn)) { di, _ -> di.cancel() }
        builder.setPositiveButton(resources.getText(R.string.ok)) { _, _ ->
            val uri = findViewById<EditText>(R.id.editTextLink).text.toString()
            val isCorrect = parseDynamicLink(uri)
            if (isCorrect != null) {
                connectToDB(isCorrect)
                dialog!!.hide()
            } else {
                toast.setText(R.string.multi_bad_link)
                toast.show()
            }
        }
        dialog = builder.create()
        dialog!!.show()
    }

    /**
     * add a listener to the match to know when it is ready to play
     *
     * @param match
     */
    private fun setListener(match: DocumentReference) {
        listener = match.addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            if (SnapshotListener.listenerOnLobby(snapshot, this, dialog!!)) {
                listener?.remove()
                /*(context as AppCompatActivity).supportFragmentManager.beginTransaction()
    .replace(
        (viewFragment.parent as ViewGroup).id,
        DemoFragment(),
        "DEMO"
    )
    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
    .commit()*/

                //TODO launch game
            }
        }
    }
}
