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


    private fun getEloReference(screenName: String): DatabaseReference {
        return userReference.child(screenName).child("userStatistics").child("elo")
    }

    fun addUser(user: User) {
        userReference.child(user.screenName).setValue(user)
    }

    fun setElo(screenName: String, elo: Int) {
        getEloReference(screenName).setValue(elo)
    }


    fun addEloListener(screenName: String, listener: ValueEventListener) {
        getEloReference(screenName).addValueEventListener(listener)
    }
    fun addUserListener(screenName: String, listener: ValueEventListener) {
            userReference.child(screenName).addValueEventListener(listener)
    }





}