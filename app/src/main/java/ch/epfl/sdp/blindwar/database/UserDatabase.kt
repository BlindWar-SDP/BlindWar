package ch.epfl.sdp.blindwar.database

import ch.epfl.sdp.blindwar.user.AppStatistics
import ch.epfl.sdp.blindwar.user.Mode
import ch.epfl.sdp.blindwar.user.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


object UserDatabase {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val userReference = database.getReference("Users")

    /**
     * Get user reference to manipulate user infos
     * @param uid
     * @return
     */
    private fun getUserReference(uid: String): DatabaseReference {
        return userReference.child(uid)
    }

    /**
     * Get user statistics reference to manipulate user statistics
     * @param uid
     * @return
     */
    private fun getUserStatisticsReference(uid: String): DatabaseReference {
        return getUserReference(uid).child("userStatistics")
    }

    /**
     * Get elo reference to manipulate elo
     * @param uid
     * @return
     */
    fun getEloReference(uid: String): DatabaseReference {
        return getUserStatisticsReference(uid).child("elo")
    }

    fun getImageReference(uid: String): DatabaseReference {
        return userReference.child(uid).child("profilePicture")
    }

    /**
     * Add User to database
     * @param uid
     * @param user
     */
    fun addUser(uid: String, user: User) {
        userReference.child(uid).setValue(user)
    }
    /*
    // Remove user from database
    fun removeUser(uid: String) {
        userReference.child(uid).removeValue()
    }*/

    // Set elo of an user
    fun setElo(uid: String, elo: Int) {
        getEloReference(uid).setValue(elo)
    }

    /**
     * Reset set user statistics
     *
     * @param uid
     */
    fun setUserStatistics(uid: String, userStatistics: AppStatistics) {
        getUserStatisticsReference(uid).setValue(userStatistics)
    }

    // Allow user to select a profile picture and store it in database
    fun addProfilePicture(uid: String, path: String) {
        getImageReference(uid).setValue(path)
    }

    fun addUserListener(uid: String, listener: ValueEventListener) {
        userReference.child(uid).addValueEventListener(listener)
    }

    /**
     * Gets UserStatistics of a user. Needs to be treated as a future(addOnSuccessListener)
     * @param uid
     * @return Task<DataSnapshot>
     */
    private fun getUserStatistics(uid: String): Task<DataSnapshot> {
        val userStatisticsRef = getUserStatisticsReference(uid)
        return userStatisticsRef.get()
    }

    fun updateSoloUserStatistics(uid: String, score: Int, fails: Int) {
        getUserStatistics(uid).addOnSuccessListener {
            var userStatistics: AppStatistics? = it.getValue(AppStatistics::class.java)
            if (userStatistics != null) {
                userStatistics.correctnessUpdate(score, fails, Mode.SOLO)
                setUserStatistics(uid, userStatistics)
            }
        }
    }
}