package ch.epfl.sdp.blindwar.data

import ch.epfl.sdp.blindwar.database.UserDatabase
import com.google.firebase.database.ValueEventListener

object UserRepository {

    private val userDatabase = UserDatabase

    fun addUserListener(uid: String, listener: ValueEventListener) {
        UserDatabase.userReference.child(uid).addValueEventListener(listener)
    }
}