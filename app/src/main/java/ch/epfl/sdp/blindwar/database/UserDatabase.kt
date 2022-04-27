package ch.epfl.sdp.blindwar.database

import ch.epfl.sdp.blindwar.profile.model.AppStatistics
import ch.epfl.sdp.blindwar.profile.model.Mode
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


object UserDatabase {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val userReference = database.getReference("Users")

    /**
     * Get user reference to manipulate user infos
     *
     * @param uid
     * @return
     */
    private fun getUserReference(uid: String): DatabaseReference {
        return userReference.child(uid)
    }

    /**
     * Get user statistics reference to manipulate user statistics
     *
     * @param uid
     * @return
     */
    private fun getUserStatisticsReference(uid: String): DatabaseReference {
        return getUserReference(uid).child("userStatistics")
    }

    /**
     * Get elo reference to manipulate elo
     *
     * @param uid user identification
     * @return elo reference of specified user
     */
    fun getEloReference(uid: String): DatabaseReference {
        return getUserStatisticsReference(uid).child("elo")
    }

    fun getImageReference(uid: String): DatabaseReference {
        return userReference.child(uid).child("profilePicture")
    }

    /**
     * Function to add an User to the database (used when creating account)
     *
     * @param user to be added
     */
    // Add user to database
    fun addUser(user: User) {
        userReference.child(user.uid).setValue(user)
    }

    /**
     * Remove user from database
     *
     * @param uid user identification
     */
    fun removeUser(uid: String) {
        userReference.child(uid).removeValue()
    }

    /**
     * Set elo of an user
     *
     * @param uid user identification
     */
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

    /**
     * Reset set user statistics
     *
     * @param uid user identification
     */
    private fun setUserStatistics(uid: String, userStatistics: AppStatistics) {
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
            val userStatistics: AppStatistics? = it.getValue(AppStatistics::class.java)
            if (userStatistics != null) {
                userStatistics.correctnessUpdate(score, fails, Mode.SOLO)
                setUserStatistics(uid, userStatistics)
            }
        }
    }

    /**
     * Get current authenticated user
     *
     */
    fun getCurrentUser(): DataSnapshot {
        return getUserReference(Firebase.auth.currentUser!!.uid).get().result
    }
}