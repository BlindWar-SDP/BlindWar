package ch.epfl.sdp.blindwar.game.multi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.MatchDatabase
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.game.multi.model.Match
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * Activity that lets the user start a multiplayer game
 *
 * @constructor creates a MultiPlayerActivity
 */
class MultiPlayerActivity : AppCompatActivity() {
    private val LIMIT_MATCH: Long = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi)
    }

    /**
     * Starts a multiplayer game played with a friend
     *
     * @param view
     */
    fun friendButton(view: View) {
        val intent = Intent(this, MultiPlayerFriendActivity::class.java)
        startActivity(intent)
    }

    /**
     * Starts a multiplayer game played with a random user
     *
     * @param view
     */
    fun randomButton(view: View) {
        //TODO launch lobby fragment
        val user = UserDatabase.getCurrentUser()
        val elo = user.child("userStatistics/elo").value!! as Int
        val matchs = Firebase.firestore.collection("match").whereLessThan("elo", elo + 200)
            .whereGreaterThan("elo", elo - 200)
            .orderBy("elo", Query.Direction.DESCENDING)
            .limit(LIMIT_MATCH).get()
        if (matchs.isSuccessful) {
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
            }
            if (match == null) {
                Toast.makeText(applicationContext, "Match are full, retrying...", Toast.LENGTH_LONG)
                    .show()
                randomButton(view)
            } else{
                match.addSnapshotListener{} //TODO CONNECT TO MATCH
            }
        } else {
            randomButton(view)
        }
    }
}
