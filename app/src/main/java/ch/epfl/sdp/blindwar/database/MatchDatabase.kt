package ch.epfl.sdp.blindwar.database

import ch.epfl.sdp.blindwar.game.model.config.GameInstance
import ch.epfl.sdp.blindwar.game.multi.model.Match
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

object MatchDatabase {
    const val COLLECTION_PATH = "match"

    /**
     * return if the player has already created a multiplayer game
     *
     * @param userUID
     * @param userPseudo
     * @param userElo
     * @param numberOfPlayerMax
     * @param game
     * @param db
     * @param isPrivate
     * @return
     */
    fun createMatch(
        userUID: String,
        userPseudo: String,
        userElo: Int,
        numberOfPlayerMax: Int = 2,
        game: GameInstance,
        db: FirebaseFirestore,
        isPrivate: Boolean = false
    ): Match {
        val match = Match(
            userUID,
            userElo,
            mutableListOf(userUID),
            mutableListOf(userPseudo),
            game,
            mutableListOf(0),
            numberOfPlayerMax,
            isPrivate
        )
        db.collection(COLLECTION_PATH).add(match)
        return match
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
        if (match.listPlayers!!.size < match.maxPlayer) return null
        match.listPlayers!!.add(user.uid)
        match.listPseudo!!.add(user.pseudo)
        match.listResult!!.add(0)
        val matchDB = db.collection(COLLECTION_PATH).document(match.uid)
        matchDB.set(match)
        return matchDB
    }

    /**
     * Get a match from a uid
     *
     * @param uid
     * @param db
     * @return
     */
    fun getMatch(uid: String, db: FirebaseFirestore): Match? {
        val match = db.collection(COLLECTION_PATH).document(uid).get().result
        return if (match.exists())
            match.toObject(Match::class.java) else null
    }
}