package ch.epfl.sdp.blindwar.database

import ch.epfl.sdp.blindwar.user.Mode
import ch.epfl.sdp.blindwar.user.User
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
     * @param mode
     * @return
     */
    fun createMatch(user: User, numberOfPlayerMax: Int, mode: Mode): Boolean {
        val match =
            Match(user.uid, mutableListOf(user), mode, mutableListOf(0), 0, numberOfPlayerMax)
        if (matchReference.child(match.uid).get().isSuccessful) return false
        matchReference.child(match.uid).setValue(match)
        return true
    }

    fun removeMatch(uid: String) {
        matchReference.child(uid).removeValue()
    }

    fun addPlayer(match: Match, user: User): Boolean {
        if (match.listPlayers!!.size + 1 > match.maxPlayer) return false
        match.listPlayers!!.add(user)
        match.listResult!!.add(0)
        matchReference.child(match.uid).setValue(match)
        return true
    }

    fun onGameUpdate(match: Match, turnWinners: MutableList<User>) {
        match.numberOfMusics += 1
        turnWinners.forEach { p -> match.listResult!![match.listPlayers!!.indexOf(p)] += 1 }
        matchReference.child(match.uid).setValue(match)
    }
}