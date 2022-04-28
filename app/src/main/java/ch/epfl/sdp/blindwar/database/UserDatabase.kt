package ch.epfl.sdp.blindwar.database

import ch.epfl.sdp.blindwar.profile.model.AppStatistics
import ch.epfl.sdp.blindwar.profile.model.Mode
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


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
    fun updateUser(user: User) {
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

//    fun updateUser(user: User){
//        val ref = userReference.child(user.uid)
//        // TODO no statistics ...
//        ref.child(User.VarName.pseudo.name).setValue(user.pseudo)
//        ref.child(User.VarName.firstName.name).setValue(user.firstName)
//        ref.child(User.VarName.lastName.name).setValue(user.lastName)
//        ref.child(User.VarName.profilePicture.name).setValue(user.profilePicture)
//        ref.child(User.VarName.description.name).setValue(user.description)
//        ref.child(User.VarName.gender.name).setValue(user.gender)
//        ref.child(User.VarName.birthdate.name).setValue(user.birthdate)
//    }

    /**
     * Reset set user statistics
     *
     * @param uid user identification
     */
    private fun setUserStatistics(uid: String, userStatistics: AppStatistics) {
        getUserStatisticsReference(uid).setValue(userStatistics)
    }

    /**
     *  Allow user to select a profile picture and store it in database
     *
     * @param uid
     * @param path
     */
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
            userStatistics?.let{stat ->
                stat.correctnessUpdate(score, fails, Mode.SOLO)
                setUserStatistics(uid, stat)
            }
        }
    }
}