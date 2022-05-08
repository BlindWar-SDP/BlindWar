package ch.epfl.sdp.blindwar.database

import ch.epfl.sdp.blindwar.game.model.config.GameInstance
import ch.epfl.sdp.blindwar.game.multi.model.Match
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
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
        numberOfPlayerMax: Int = 2,
        game: GameInstance,
        db: FirebaseFirestore,
        isPrivate: Boolean = false
    ) {
         db.collection(COLLECTION_PATH).add(
            Match(
                user.uid,
                user.userStatistics.elo,
                mutableListOf(user.uid),
                mutableListOf(user.pseudo),
                game,
                mutableListOf(0),
                numberOfPlayerMax,
                isPrivate
            )
        )
    }

    /**
     * delete match when done
     *
     * @param uid
     */
    fun removeMatch(uid: String, db: FirebaseFirestore): Boolean {
        db.collection(COLLECTION_PATH).document(uid).delete()
        return true
    }

    /**
     * add a player to the match and return the document
     *
     * @param match
     * @param user
     * @return
     */
    fun connect(match: Match, user: User, db: FirebaseFirestore): DocumentReference? {
        if (match.listPlayers!!.size + 1 > match.maxPlayer) return null
        match.listPlayers!!.add(user.uid)
        match.listPseudo!!.add(user.pseudo)
        match.listResult!!.add(0)
        val matchDB = db.collection(COLLECTION_PATH).document(match.uid)
        matchDB.set(match)
        return matchDB
    }
}