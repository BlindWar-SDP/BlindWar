package ch.epfl.sdp.blindwar.database

import ch.epfl.sdp.blindwar.user.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


object UserDatabase {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val userReference = database.getReference("Users")

    // Get an elo reference for manipulating the elo data of an user
    fun getEloReference(uid: String): DatabaseReference {
        return userReference.child(uid).child("userStatistics").child("elo")
    }

    fun getImageReference(uid: String): DatabaseReference {
        return userReference.child(uid).child("profilePicture")
    }

    // Add user to database
    fun addUser(user: User) {
        userReference.child(user.uid).setValue(user)
    }
    // Remove user from database
    fun removeUser(uid: String) {
        userReference.child(uid).removeValue()
    }

    // Set elo of an user
    fun setElo(uid: String, elo: Int) {
        getEloReference(uid).setValue(elo)
    }
    fun setFirstName(uid: String, fn: String) {
        userReference.child(uid).child("firstName").setValue(fn)
    }
    fun setLastName(uid: String, ln: String) {
        userReference.child(uid).child("lastName").setValue(ln)
    }
    fun setPseudo(uid: String, pseudo: String) {
        userReference.child(uid).child("pseudo").setValue(pseudo)
    }
    fun setProfilePicture(uid: String, pp: String) {
        userReference.child(uid).child("profilePicture").setValue(pp)
    }
    fun setBirthdate(uid: String, date: Long) {
        userReference.child(uid).child("birthDate").setValue(date)
    }
    fun setGender(uid: String, gender: String) {
        userReference.child(uid).child("gender").setValue(gender)
    }
    fun setDescription(uid: String, desc: String) {
        userReference.child(uid).child("description").setValue(desc)
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