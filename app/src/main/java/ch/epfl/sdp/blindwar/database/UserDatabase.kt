package ch.epfl.sdp.blindwar.database

import ch.epfl.sdp.blindwar.ui.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// Currently: it's possible to add an user, set it's elo and add listener
class UserDatabase {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val userReference = database.getReference("Users")

    // Get an elo reference for manipulating the elo data of an user
    private fun getEloReference(uid: String): DatabaseReference {
        return userReference.child(uid).child("userStatistics").child("elo")
    }

    private fun getImageReference(uid: String): DatabaseReference {
        return userReference.child(uid).child("image")
    }

    // Add user to database
    fun addUser(uid: String, user: User) {
        userReference.child(uid).setValue(user)
    }

    // Remove user from database
    fun removeUser(uid: String) {
        userReference.child(uid).removeValue()
    }

    // Set elo of an user
    fun setElo(uid: String, elo: Int) {
        getEloReference(uid).setValue(elo)
    }

    // Allow user to select a profile picture and store it in database
    fun addProfilePicture(uid: String, path: String) {
        getImageReference(uid).setValue(path)
    }

    /*
    fun addEloListener(screenName: String, listener: ValueEventListener) {
        getEloReference(screenName).addValueEventListener(listener)
    }
    */
    fun addUserListener(uid: String, listener: ValueEventListener) {
        userReference.child(uid).addValueEventListener(listener)
    }
}