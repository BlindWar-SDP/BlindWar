package ch.epfl.sdp.blindwar.profile.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.blindwar.data.music.metadata.URIMusicMetadata
import ch.epfl.sdp.blindwar.database.ImageDatabase
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.game.model.GameResult
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.StorageReference

class ProfileViewModel : ViewModel() {

    // OBSERVABLES
    val user = MutableLiveData<User>()
    val imageRef = MutableLiveData<StorageReference>()

    private val userInfoListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get User info and use the values to update the UI
            val userDB: User? = try {
                dataSnapshot.getValue<User>()
            } catch (e: DatabaseException) {
                null
            }

            userDB?.let {
                user.postValue(it)
                Log.d("ZAMBO ANGUISSA", it.firstName)

                if (it.profilePicture.isNotEmpty()) {
                    imageRef.postValue(ImageDatabase.getImageReference(it.profilePicture))
                }

            } ?: run {
                user.postValue(User.Builder().build())
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }

    init {
        FirebaseAuth.getInstance().currentUser?.let {
            UserDatabase.addUserListener(it.uid, userInfoListener)
        }
    }

    fun updateStats(score: Int, fails: Int, gameResult: GameResult) {
        FirebaseAuth.getInstance().currentUser?.let {
            UserDatabase.updateSoloUserStatistics(it.uid, score, fails)
            UserDatabase.addGameResult(it.uid, gameResult)
        }
    }

    fun likeMusic(music: URIMusicMetadata) {
        FirebaseAuth.getInstance().currentUser?.let {
            UserDatabase.addLikedMusic(it.uid, music)
        }
    }
}