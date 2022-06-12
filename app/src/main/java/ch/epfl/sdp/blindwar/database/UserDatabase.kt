package ch.epfl.sdp.blindwar.database

import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata
import ch.epfl.sdp.blindwar.game.model.GameResult
import ch.epfl.sdp.blindwar.game.model.config.GameFormat
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


object UserDatabase {
    private const val COLLECTION_PATH = "Users"


    val userRef = Firebase.firestore.collection(COLLECTION_PATH)

    fun userDoc(uid: String): DocumentReference {
        return userRef.document(uid)
    }

    /**
     * Function to add an User to the database (used when creating account)
     *
     * @param user to be added
     */
    fun updateUser(user: User): Task<Void>?{
        return if (user.uid.isNotEmpty()) {
            userDoc(user.uid).set(user)
        } else null
    }

    /**
     * Set matchId of the uid user to null
     *
     * @param uid
     */
    fun removeMatchId(uid: String) {
        userDoc(uid).update(mapOf("matchID" to ""))
    }

    /**
     * Set matchId of the uid user
     *
     * @param uid
     */
    fun addMatchId(uid: String, matchId: String) {
        userDoc(uid).update(mapOf("matchID" to matchId))

    }

    /**
     * Remove user from database
     *
     * @param uid user identification
     */
    fun removeUser(uid: String) {
        userDoc(uid).delete()
    }

    /**
     * Function to add a liked music in user's list of liked music (in particular when he presses
     * the like button)
     * @param uid
     * @param music
     */
    fun addLikedMusic(uid: String, music: MusicMetadata): Task<DocumentSnapshot> {
        return userDoc(uid).get().addOnSuccessListener { snapshot ->
            snapshot.toObject(User::class.java)?.let { user ->
                var duplicate = false
                for (likedMusic in user.likedMusics) {
                    if (music.name == likedMusic.name) {
                        duplicate = true
                    }
                }
                if (!duplicate) {
                    user.likedMusics.add(music)
                    updateUser(user)
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
        userDoc(uid).get().addOnSuccessListener { snapshot ->
            snapshot.toObject(User::class.java)?.let { user ->
                user.matchHistory.add(gameResult)
                updateUser(user)
            }
        }
    }

    /**
     *  Allow user to select a profile picture and store it in database
     *
     * @param uid
     * @param path
     */
    fun addProfilePicture(uid: String, path: String) {
        userDoc(uid).update(mapOf("profilePicture" to path))
    }

    /**
     * Gets the userStatistics of the user from the database and update its statistics
     * using the score of the game.
     * @param uid
     * @param score
     * @param fails
     */
    fun updateSoloUserStatistics(uid: String, score: Int, fails: Int): Task<DocumentSnapshot> {
        return userDoc(uid).get().addOnSuccessListener { snapshot ->
            snapshot.toObject(User::class.java)?.let { user ->
                val stats = user.userStatistics
                stats.correctnessUpdate(score, fails, GameFormat.SOLO)
                user.userStatistics = stats
                updateUser(user)
            }
        }
    }

    /**
     * Get current authenticated user
     */
    fun getCurrentUser(): User? {
        var out: User? = null
        Firebase.auth.currentUser?.let { auth ->
            userDoc(auth.uid).get().addOnSuccessListener { snapshot ->
                snapshot.toObject(User::class.java)?.let { user ->
                    out = user
                }
            }
        }
        return out
    }
}