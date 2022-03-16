package ch.epfl.sdp.blindwar.database

import ch.epfl.sdp.blindwar.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserDatabase {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()


    fun addUser(user: User){
        val userReference = database.getReference("Users")
        userReference.child(user.screenName).setValue(user)

    }

}