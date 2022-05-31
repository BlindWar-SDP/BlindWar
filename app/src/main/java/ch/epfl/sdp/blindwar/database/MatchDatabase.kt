@file:Suppress("ControlFlowWithEmptyBody", "ControlFlowWithEmptyBody")

package ch.epfl.sdp.blindwar.database

import ch.epfl.sdp.blindwar.game.model.config.GameInstance
import ch.epfl.sdp.blindwar.game.multi.model.Match
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
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
    ): Match? {
        if (userUID.isNotEmpty()) {
            val match = Match(
                userUID,
                userElo,
                mutableListOf(userUID),
                mutableListOf(userPseudo),
                game,
                mutableListOf(0),
                mutableListOf(false),
                numberOfPlayerMax,
                isPrivate
            )
            db.collection(COLLECTION_PATH).document(match.uid).set(match)
            UserDatabase.addMatchId(userUID, match.uid)
            return match
        }
        return null
    }

    /**
     * Delete match when done
     * TODO update elo ?
     * @param uid
     */
    fun removeMatch(uid: String, db: FirebaseFirestore): Boolean {
        val matchDocument = getMatchSnapshot(uid, db)
        if (matchDocument != null) {
            val match = matchDocument.toObject(Match::class.java)
            match?.listPlayers?.forEach { it ->
                UserDatabase.removeMatchId(it)
            }
            db.collection(COLLECTION_PATH).document(uid).delete()
            return true
        }
        return false
    }

    /**
     * add a player to the match and return the document
     *
     * @param match
     * @param user
     * @return
     */
    fun connect(match: Match, user: User, db: FirebaseFirestore): DocumentReference? {
        if (match.listPlayers!!.size >= match.maxPlayer) return null
        if (user.matchId.isNotEmpty()) return null
        if (match.listPlayers!!.contains(user.uid)) return null
        match.listPlayers!!.add(user.uid)
        match.listPseudo!!.add(user.pseudo)
        match.listResult!!.add(0)
        match.listFinished!!.add(false)
        UserDatabase.addMatchId(user.uid, match.uid)
        db.collection(COLLECTION_PATH).document(match.uid).set(match)
        return db.collection(COLLECTION_PATH).document(match.uid)
    }

    /**
     * Get match snapshot from a uid
     *
     * @param uid
     * @param db
     * @return
     */
    fun getMatchSnapshot(uid: String, db: FirebaseFirestore): DocumentSnapshot? {
        val query = db.collection(COLLECTION_PATH).whereEqualTo("uid", uid).limit(1).get()
        while (!query.isComplete); //TODO avoid active waiting
        return query.result.documents[0]
    }

    fun getMatchReference(uid: String, db: FirebaseFirestore): DocumentReference {
        return db.collection(COLLECTION_PATH).document(uid)
    }

    /**
     * Increments the score of a player in the list of score for a specific match.
     * For this we need to do a transaction, because multiple players may have the score
     * updated at the same time.
     * @param matchId
     * @param playerIndex
     */
    fun incrementScore(matchId: String, playerIndex: Int, db: FirebaseFirestore) {
        val matchRef = getMatchReference(matchId, db)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(matchRef)
            val match = snapshot.toObject(Match::class.java)
            val listScore = match?.listResult
            listScore!![playerIndex] += 1
            transaction.update(matchRef, "listResult", listScore)

            // Success
            null
        }
    }

    /**
     * Listen to changes in the score(actually we can only listen to changes in the document
     * and not a specific field)
     * @param matchId
     * @param db
     */

    fun addListener(
        matchId: String,
        db: FirebaseFirestore,
        listener: EventListener<DocumentSnapshot>
    ) {
        val matchRef = getMatchReference(matchId, db)
        matchRef.addSnapshotListener(listener)
    }

    /**
     * Tell the server that a specific player finished the game
     *
     * @param matchId
     * @param playerIndex
     * @param db
     */
    fun playerFinish(matchId: String, playerIndex: Int, db: FirebaseFirestore) {
        val matchRef = getMatchReference(matchId, db)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(matchRef)
            val match = snapshot.toObject(Match::class.java)
            val listFinished = match?.listFinished
            listFinished!![playerIndex] = true
            transaction.update(matchRef, "listFinished", listFinished)

            // Success
            null
        }
    }
}