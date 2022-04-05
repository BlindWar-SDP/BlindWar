package ch.epfl.sdp.blindwar.database

import com.google.firebase.database.FirebaseDatabase

object MatchDatabase {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val matchReference = database.getReference("Match")
}   