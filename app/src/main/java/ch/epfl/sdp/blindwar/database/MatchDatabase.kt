package ch.epfl.sdp.blindwar.database

import ch.epfl.sdp.blindwar.game.model.config.GameInstance
import ch.epfl.sdp.blindwar.game.multi.model.Match
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object MatchDatabase {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val matchReference = database.getReference("Match")

    private fun getMatchReference(uid: String): DatabaseReference {
        return matchReference.child(uid)
    }

    /**
     * return if the player has already created a multiplayer game
     *
     * @param user
     * @param numberOfPlayerMax
     * @param game gameInstance
     * @return
     */
    fun createMatch(user: User, numberOfPlayerMax: Int, game: GameInstance): Boolean {
        val match =
            Match(user.uid, mutableListOf(user), game, mutableListOf(0), numberOfPlayerMax)
        if (matchReference.child(match.uid).get().isSuccessful) return false
        matchReference.child(match.uid).setValue(match)
        return true
    }

    /**
     * delete match when done
     *
     * @param uid
     */
    fun removeMatch(uid: String) {
        matchReference.child(uid).removeValue()
    }

    /**
     * add a player to the match (connexion)
     *
     * @param match
     * @param user
     * @return
     */
    fun addPlayer(match: Match, user: User): Boolean {
        if (match.listPlayers!!.size + 1 > match.maxPlayer) return false
        match.listPlayers!!.add(user)
        match.listResult!!.add(0)
        matchReference.child(match.uid).setValue(match)
        return true
    }

    /**
     * update game to send to other players
     *
     * @param match
     * @param turnWinners
     */
    fun onGameUpdate(match: Match, turnWinners: MutableList<User>) {
        turnWinners.forEach { p -> match.listResult!![match.listPlayers!!.indexOf(p)] += 1 }
        matchReference.child(match.uid).setValue(match)
    }
}