package ch.epfl.sdp.blindwar.database

import ch.epfl.sdp.blindwar.game.model.config.GameInstance
import ch.epfl.sdp.blindwar.game.multi.model.Match
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.firebase.firestore.FirebaseFirestore

object MatchDatabase {
    private const val COLLECTION_PATH = "match"

    /**
     * return if the player has already created a multiplayer game
     *
     * @param user
     * @param numberOfPlayerMax
     * @param game gameInstance
     * @return
     */
    fun createMatch(
        user: User,
        numberOfPlayerMax: Int,
        game: GameInstance,
        db: FirebaseFirestore
    ): Boolean {
        db.collection(COLLECTION_PATH).add(
            Match(user.uid, mutableListOf(user), game, mutableListOf(0), numberOfPlayerMax)
        ).addOnSuccessListener {
            //return true
        }.addOnFailureListener {
            //return false
        }
        return true
    }

    /**
     * delete match when done
     *
     * @param uid
     */
    fun removeMatch(uid: String, db: FirebaseFirestore): Boolean {
        db.collection(COLLECTION_PATH).document(uid).delete()
            .addOnSuccessListener { }
            .addOnFailureListener {}
        return true
    }

    /**
     * add a player to the match (connexion)
     *
     * @param match
     * @param user
     * @return
     */
    fun addPlayer(match: Match, user: User, db: FirebaseFirestore): Boolean {
        if (match.listPlayers!!.size + 1 > match.maxPlayer) return false
        match.listPlayers!!.add(user)
        match.listResult!!.add(0)
        db.collection(COLLECTION_PATH).document(match.uid)
            .set(match)
            .addOnSuccessListener { }
            .addOnFailureListener {}
        return true
    }
}