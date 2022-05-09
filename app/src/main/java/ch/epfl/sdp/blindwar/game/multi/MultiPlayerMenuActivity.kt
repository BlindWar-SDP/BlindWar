package ch.epfl.sdp.blindwar.game.multi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.menu.MainMenuActivity


/**
 * Activity that lets the user start a multiplayer game
 *
 * @constructor creates a MultiPlayerMenuActivity
 */
class MultiPlayerMenuActivity : AppCompatActivity() {
    private var eloDelta = 200
    private var dialog: AlertDialog? = null
    private var isCanceled = false
    private lateinit var toast: Toast

    companion object {
        private val LIMIT_MATCH: Long = 10
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
        setLinkDialog()
        //dialog!!.hide() //TODO REMOVE WHEN TESTS OK
    }

    /**
     * Launch the create match activity
     *
     * @param view
     */
    fun createMatch(view: View) {
        startActivity(Intent(this, ChoseNumberOfPlayerActivity::class.java))
    }

    /**
     * Cancel
     *
     * @param view
     */
    fun cancel(view: View) {
        startActivity(Intent(this, MainMenuActivity::class.java))
    }

    /**
     * Starts a multiplayer game played with a random user
     *
     * @param view
     */
    fun randomButton(view: View) {
        //setProgressDialog(getString(R.string.multi_wait_matches))
        /*val user = UserDatabase.getCurrentUser()
        val elo = user.child("userStatistics/elo").value!! as Int
        val matchs = Firebase.firestore.collection("match").where("isPrivate", false).whereLessThan("elo", elo + eloDelta)
            .whereGreaterThan("elo", elo - 200)
            .orderBy("elo", Query.Direction.DESCENDING)
            .limit(LIMIT_MATCH).get()
        if (matchs.isSuccessful && !isCanceled) {
            var i = 0
            var match: DocumentReference? = null
            while (match == null && i < LIMIT_MATCH) {
                match =
                    MatchDatabase.connect(
                        matchs.result.documents[i].toObject(Match::class.java)!!,
                        user.getValue(User::class.java)!!,
                        Firebase.firestore
                    )
                i++
            }discord
            if (match == null && !isCanceled) {
                toast.setText(getString(R.string.toast_connexion))
                toast.show()
                eloDelta += 100
                randomButton(view)
            } else if (!isCanceled) {
                //match.addSnapshotListener {} //TODO add listener
                dialog!!.hide()
                //TODO CONNECT TO MATCH
            }
        } else if (!isCanceled) {
            randomButton(view)
        }*/
        //dialog!!.hide() //TODO REMOVE WHEN TESTS OK
    }

    /**
     * display progressDialog cancelable for any messages
     *
     * @param message
     */
    /*
    private fun setProgressDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
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
     */

    /**
     * find a match on DB which is free
     *
     * @param text
     */
    /*
    private fun connectToDB(text: String) {
        setProgressDialog("Wait for connexion")
        val user = UserDatabase.getCurrentUser()
        val match = Firebase.firestore.collection("match").document(text).get() //TODO get only uid
        if (match.isSuccessful && !isCanceled) {
            val connect =
                MatchDatabase.connect(
                    match.result.toObject(Match::class.java)!!,
                    user.getValue(User::class.java)!!,
                    Firebase.firestore
                )
            if (connect == null && !isCanceled) {
                toast.setText(getString(R.string.multi_match_full))
                toast.show()
                dialog!!.hide()
            } else if (!isCanceled) {
                //match.addSnapshotListener {} //TODO add listener
                dialog!!.hide()
                //TODO CONNECT TO MATCH
            }
        } else if (!isCanceled) {
            toast.setText(getString(R.string.multi_match_not_found))
            toast.show()
            dialog!!.hide()
        }
    }
     */

    /**
     * create a dialog which ask for the uid of the match
     *
     */
    private fun setLinkDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        val view = View.inflate(applicationContext, R.layout.fragment_multi_connexion_link, null)
        builder.setView(view)
        builder.setNeutralButton(resources.getText(R.string.cancel_btn)) { _, _ ->
            dialog!!.hide()
        }
        builder.setPositiveButton(resources.getText(R.string.ok)) { _, _ ->
            //connectToDB(findViewById<EditText>(R.id.editTextLink).text.toString())
            dialog!!.hide()
        }
        dialog = builder.create()
        dialog!!.show()
    }
}
