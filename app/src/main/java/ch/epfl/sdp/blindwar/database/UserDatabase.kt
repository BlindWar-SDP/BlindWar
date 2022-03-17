package ch.epfl.sdp.blindwar.database

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.TextView
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.User
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

// Currently: it's possible to add an user, set it's elo and add listener
class UserDatabase {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val userReference = database.getReference("Users")

    // Get an elo reference for manipulating the elo data of an user
    private fun getEloReference(screenName: String): DatabaseReference {
        return userReference.child(screenName).child("userStatistics").child("elo")
    }

    // Add user to database
    fun addUser(user: User) {
        userReference.child(user.screenName).setValue(user)
    }

    // Remove user from database
    fun removeUser(screenName: String) {
        userReference.child(screenName).removeValue()
    }

    // Set elo of an user
    fun setElo(screenName: String, elo: Int) {
        getEloReference(screenName).setValue(elo)
    }

    /*
    fun addEloListener(screenName: String, listener: ValueEventListener) {
        getEloReference(screenName).addValueEventListener(listener)
    }
    */
    fun addUserListener(screenName: String, listener: ValueEventListener) {
            userReference.child(screenName).addValueEventListener(listener)
    }





}