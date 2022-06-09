package ch.epfl.sdp.blindwar.game.multi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.URLUtil
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.MatchDatabase
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.game.multi.model.Match
import ch.epfl.sdp.blindwar.game.solo.fragments.DemoFragment
import ch.epfl.sdp.blindwar.game.util.DynamicLinkHelper
import ch.epfl.sdp.blindwar.game.util.MainMusic
import ch.epfl.sdp.blindwar.login.PermissionHandler
import ch.epfl.sdp.blindwar.menu.MainMenuActivity
import ch.epfl.sdp.blindwar.profile.fragments.DisplayHistoryFragment
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


/**
 * Activity that lets the user start a multiplayer game
 *
 * @constructor creates a MultiPlayerMenuActivity
 */
class MultiPlayerMenuActivity : AppCompatActivity() {
    private var eloDelta = DEFAULT_ELO
    private var dialog: AlertDialog? = null
    private var listener: ListenerRegistration? = null
    private var toast: Toast? = null
    private var currentUser: User? = null
    private var matchId: String? = null
    private lateinit var leaderboardButton: ImageButton

    companion object {
        private const val LIMIT_MATCH = 10L
        private const val DELTA_MATCHMAKING = 100
        private const val DEFAULT_ELO = 200
        const val DYNAMIC_LINK = "Dynamic link"

        /**v
         * Launch the game for every player
         *
         * @param matchId
         */
        fun launchGame(matchId: String, supportFragmentManager: FragmentManager) {
            /* Goal: have the game with same config launched for all users
             Then we need to launch the fragment with a specific match_id as the tag
             so that the fragments knows it has to fetch value from a particular tag.*/
            val demoFragment = DemoFragment()
            val bundle = Bundle().apply {
                putString("match_id", matchId)
            }
            demoFragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                .replace(
                    android.R.id.content,
                    demoFragment,
                    "DEMO"
                )
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainMusic.play()

        setContentView(R.layout.activity_multiplayer_menu)
        currentUser = UserDatabase.getCurrentUser()
        matchId = currentUser?.matchId

        if (matchId != null && matchId!!.isNotEmpty()) {
            findViewById<FrameLayout>(R.id.frameLayout_create).visibility = View.GONE
            findViewById<FrameLayout>(R.id.frameLayout_join).visibility = View.VISIBLE
            findViewById<FrameLayout>(R.id.frameLayout_link).visibility = View.GONE
            findViewById<FrameLayout>(R.id.frameLayout_play).visibility = View.GONE
            findViewById<FrameLayout>(R.id.frameLayout_quit).visibility = View.VISIBLE
        } else {
            findViewById<FrameLayout>(R.id.frameLayout_create).visibility = View.VISIBLE
            findViewById<FrameLayout>(R.id.frameLayout_join).visibility = View.GONE
            findViewById<FrameLayout>(R.id.frameLayout_link).visibility = View.VISIBLE
            findViewById<FrameLayout>(R.id.frameLayout_play).visibility = View.VISIBLE
            findViewById<FrameLayout>(R.id.frameLayout_quit).visibility = View.GONE
        }
        eloDelta = DEFAULT_ELO
        val matchUID: String? = intent.extras?.getString(DYNAMIC_LINK)
        if (matchUID != null && (matchId == null || matchId!!.isEmpty())) {
            connectToDB(matchUID)
        }

        // Add leaderboardButton onClick
        leaderboardButton = findViewById<ImageButton>(R.id.leaderboardButton).apply {
            this.setOnClickListener {
                showFragment(
                    DisplayHistoryFragment.newInstance("leaderboard")
                )
            }
        }
        PermissionHandler.handle(applicationContext, this)
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
        /*if (dialog == null || !dialog!!.isShowing)
            setProgressDialog(getString(R.string.multi_wait_matches))
        val user = UserDatabase.getCurrentUser()
        if (user != null) {
            val elo = user.child("userStatistics/elo").value as Long
            var isOk = false
            while (!isOk) {
                val matches = Firebase.firestore.collection("match")
                    .whereEqualTo("isPrivate", false)
                    .whereLessThan("elo", elo + eloDelta)
                    .whereGreaterThan("elo", elo - eloDelta)
                    .orderBy("elo", Query.Direction.DESCENDING)
                    .limit(LIMIT_MATCH).get()
                if (matches.isSuccessful) {
                    var i = 0
                    var match: DocumentReference? = null
                    while (match == null && i < LIMIT_MATCH) {
                        match = MatchDatabase.connect(
                            matches.result.documents[i].toObject(Match::class.java)!!,
                            user.getValue(User::class.java)!!,
                            Firebase.firestore
                        )
                        i++
                    }
                    if (match == null) {
                        displayToast(R.string.toast_connexion)
                        eloDelta += DELTA_MATCHMAKING
                        isOk = false
                    } else {
                        isOk = true
                        setListener(match)
                    }
                } else {
                    displayToast(R.string.toast_connexion_internet)
                    isOk = false
                }
            }
        } else {
            displayToast(R.string.toast_connexion_internet)
            randomButton(view)
        }*/
    }

    /**
     * display progressDialog cancelable for any messages
     *
     * @param message
     */
    private fun setProgressDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setPositiveButton(
            getString(R.string.cancel_btn)
        ) { it, _ -> it.cancel() }
        builder.setOnCancelListener {
            displayToast(R.string.toast_canceled_connexion)
            quitMatch(Button(applicationContext))
        }
        val view = View.inflate(applicationContext, R.layout.fragment_dialog_loading, null)
        builder.setView(view)
        (view.findViewById<TextView>(R.id.textView_multi_loading)).text = message
        dialog = builder.create()
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.show()
    }

    /**
     * return null if dynamic link is false, otherwise return the match uid
     *
     * @param uri the dynamic link
     * @return
     */
    private fun parseDynamicLink(uri: Uri?): String? {
        if (uri != null && URLUtil.isValidUrl(uri.toString())) {
            val parameter = uri.getQueryParameter("uid")
            if (parameter != null) return parameter
            val browserIntent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(browserIntent)
        }
        return null
    }

    /**
     * find a match with the link on DB which is free
     *
     * @param uid
     */
    private fun connectToDB(uid: String) {
        setProgressDialog("Wait for connexion")
        val user = UserDatabase.getCurrentUser()
        val match: DocumentSnapshot? = MatchDatabase.getMatchSnapshot(uid, Firebase.firestore)
        if (match != null) {
            val matchObject = match.toObject(Match::class.java)!!
            val connect =
                MatchDatabase.connect(
                    matchObject,
                    user!!,
                    Firebase.firestore
                )
            if (connect == null) {
                displayToast(R.string.multi_match_full)
                dialog!!.hide()
            } else {
                setListener(match.reference)
            }
        } else {
            displayToast(R.string.multi_match_not_found)
            dialog!!.hide()
        }
    }

    /**
     * create a dialog which ask for the uid of the match
     *
     */
    private fun setLinkDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        val view = View.inflate(applicationContext, R.layout.fragment_multi_connexion_link, null)
        val editText = view.findViewById<EditText>(R.id.editTextLink)
        builder.setView(view)
        builder.setNeutralButton(resources.getText(R.string.cancel_btn)) { di, _ -> di.cancel() }
        builder.setPositiveButton(resources.getText(R.string.ok)) { _, _ ->
            val link = parseDynamicLink(editText.text.toString().toUri())
            if (link != null) {
                connectToDB(link)
                dialog!!.hide()
            } else {
                displayToast(R.string.multi_bad_link)
            }
        }
        dialog = builder.create()
        dialog!!.setCanceledOnTouchOutside(false)
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
                launchGame(match.id, supportFragmentManager)
            }
        }
    }

    /**
     * Display a toast from a string resource and cancel any toast currently displayed
     *
     * @param message
     */
    private fun displayToast(message: Int) {
        toast?.cancel()
        toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    /**
     * Quit current match
     *
     * @param view
     */
    fun quitMatch(view: View) {
        assert(view.isEnabled)
        UserDatabase.removeMatchId(currentUser!!.uid)
        finish()
        startActivity(intent)
    }

    /**
     * Join current match
     *
     * @param view
     */
    fun joinMatch(view: View) {
        val snapshot = MatchDatabase.getMatchSnapshot(matchId!!, Firebase.firestore)
        val match = snapshot?.toObject(Match::class.java)
        if (snapshot != null && match != null) {
            if (match.isStarted) {
                displayToast(R.string.multi_already_started)
                quitMatch(view)
            } else {
                if (match.listPlayers?.get(0)
                        .equals(currentUser!!.uid)
                    && match.listPlayers?.get(0) != null
                ) {
                    dialog = DynamicLinkHelper.setDynamicLinkDialog(
                        getString(R.string.multi_wait_players), match.uid, this, true
                    )
                    dialog?.show()
                    listener = Firebase.firestore.collection(MatchDatabase.COLLECTION_PATH)
                        .document(match.uid).addSnapshotListener { snapshotListener, e ->
                            if (e != null) {
                                return@addSnapshotListener
                            }
                            if (SnapshotListener.listenerOnLobby(
                                    snapshotListener, this, dialog!!
                                )
                            ) {
                                listener?.remove()
                                launchGame(
                                    match.uid,
                                    supportFragmentManager
                                )
                            }
                        }
                } else {
                    setListener(snapshot.reference)
                    setProgressDialog("Wait for connexion")
                }
            }
        } else {
            displayToast(R.string.multi_match_not_found)
            quitMatch(view)
        }
    }

    /** Shows the selected fragment
     *
     * @param fragment to show
     */
    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_multiplayer, fragment)
            addToBackStack(null)
            commit()
        }
    }
}
