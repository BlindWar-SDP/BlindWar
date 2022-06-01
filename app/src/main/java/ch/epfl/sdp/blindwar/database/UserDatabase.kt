package ch.epfl.sdp.blindwar.database

import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata
import ch.epfl.sdp.blindwar.game.model.GameResult
import ch.epfl.sdp.blindwar.game.model.config.GameFormat
import ch.epfl.sdp.blindwar.profile.model.AppStatistics
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


object UserDatabase {
    private const val COLLECTION_PATH = "Users"
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val userReference = database.getReference(COLLECTION_PATH)

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

    /**
     * Get image reference
     *
     * @param uid user id
     * @return database reference of the selected object
     */
    fun getImageReference(uid: String): DatabaseReference {
        return userReference.child(uid).child("profilePicture")
    }

    /**
     * Function to add an User to the database (used when creating account)
     *
     * @param user to be added
     */
    // Add user to database
    fun updateUser(user: User): Task<Void> {
        // return task only for test... maybe should be modified
        return userReference.child(user.uid).setValue(user)
    }

    /**
     * Set matchId of the uid user to null
     *
     * @param uid
     */
    fun removeMatchId(uid: String) {
        userReference.child(uid).updateChildren(mapOf("matchId" to ""))
    }

    /**
     * Set matchId of the uid user
     *
     * @param uid
     */
    fun addMatchId(uid: String, matchId: String) {
        userReference.child(uid).updateChildren(mapOf("matchId" to matchId))
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
     * Function to add a liked music in user's list of liked music (in particular when he presses
     * the like button)
     * @param uid
     * @param music
     */
    fun addLikedMusic(uid: String, music: MusicMetadata): Task<DataSnapshot> {
        val userRef = getUserReference(uid)
        return userRef.get().addOnSuccessListener {
            val user: User? = it.getValue(User::class.java)
            if (user != null) {
                var duplicate = false
                for (likedMusic in user.likedMusics) {
                    if (music.name == likedMusic.name) {
                        duplicate = true
                    }
                }
                if (!duplicate) {
                    user.likedMusics.add(music)
                    userRef.setValue(user)
                }
            }
        }
    }

    /**
     * Add the gameResult to the matchHistory of the user.
     * @param uid
     * @param gameResult
     */
    fun addGameResult(uid: String, gameResult: GameResult) {
        val userRef = getUserReference(uid)
        userRef.get().addOnSuccessListener {
            val user: User? = it.getValue(User::class.java)
            if (user != null) {
                user.matchHistory.add(gameResult)
                userRef.setValue(user)
            }
        }
    }

    /**
     * Reset set user statistics
     *
     * @param uid user identification
     */
    private fun setUserStatistics(uid: String, userStatistics: AppStatistics): Task<Void> {
        return getUserStatisticsReference(uid).setValue(userStatistics)
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

    /**
     * Add a Listener for a specific User(specified by uid).
     * @param uid
     * @param listener
     */
    fun addUserListener(uid: String, listener: ValueEventListener) {
        userReference.child(uid).addValueEventListener(listener)
    }

    /**
     * Used to get Pseudo and Elo of all users once.
     */
    fun addSingleEventAllUsersListener(listener: ValueEventListener) {
        userReference.addListenerForSingleValueEvent(listener)
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

    /**
     * Gets the userStatistics of the user from the database and update its statistics
     * using the score of the game.
     * @param uid
     * @param score
     * @param fails
     */
    fun updateSoloUserStatistics(uid: String, score: Int, fails: Int): Task<DataSnapshot> {
        return getUserStatistics(uid).addOnSuccessListener {
            val userStatistics: AppStatistics? = it.getValue(AppStatistics::class.java)
            userStatistics?.let { stat ->
                stat.correctnessUpdate(score, fails, GameFormat.SOLO)
                setUserStatistics(uid, stat)
            }
        }
    }

    /**
     * Get current authenticated user
     * TODO check when null if internet is ok
     */
    fun getCurrentUser(): DataSnapshot? {
        return try {
            val task = getUserReference(Firebase.auth.currentUser!!.uid).get()
            while (!task.isComplete);
            if (task.isSuccessful) task.result else null
        } catch (_: Exception) {
            null
        }
    }

    /**
     * set keepSynced to true for the User
     * keep cache to prevent connection loss
     *
     * @param uid
     */
    fun setKeepSynced(uid: String) {
        getUserReference(uid).keepSynced(true)
    }
}