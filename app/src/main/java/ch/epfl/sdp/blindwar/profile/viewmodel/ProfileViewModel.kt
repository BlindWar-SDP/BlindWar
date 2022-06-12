package ch.epfl.sdp.blindwar.profile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata
import ch.epfl.sdp.blindwar.database.ImageDatabase
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.game.model.GameResult
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference

class ProfileViewModel : ViewModel() {

    // OBSERVABLES
    val imageRef = MutableLiveData<StorageReference?>()
    val user = MutableLiveData<User>()

    init {
        FirebaseAuth.getInstance().currentUser?.let {
            UserDatabase.userDoc(it.uid).get().addOnSuccessListener { snapshot ->
                snapshot.toObject(User::class.java)?.let { user0 ->
                    if (user0.profilePicture.isNotEmpty()) {
                        imageRef.postValue(ImageDatabase.getImageReference(user0.profilePicture))
                    } else {
                        imageRef.postValue(null)
                    }
                    user.postValue(user0)
                }
            }
        }
    }


    /**
     * logout user from firebase object
     *
     */
    fun logout() {
        Firebase.auth.signOut()
    }

    /**
     * Update statistics
     *
     * @param score
     * @param fails
     * @param gameResult
     */
    fun updateStats(score: Int, fails: Int, gameResult: GameResult) {
        FirebaseAuth.getInstance().currentUser?.let { auth ->
            UserDatabase.updateSoloUserStatistics(auth.uid, score, fails).addOnSuccessListener {
                UserDatabase.addGameResult(auth.uid, gameResult)
            }
        }
    }

    /**
     * Like a music
     *
     * @param music
     */
    fun likeMusic(music: MusicMetadata) {
        FirebaseAuth.getInstance().currentUser?.let {
            UserDatabase.addLikedMusic(it.uid, music)
        }
    }
}