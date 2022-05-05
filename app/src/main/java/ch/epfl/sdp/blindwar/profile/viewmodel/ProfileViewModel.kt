package ch.epfl.sdp.blindwar.profile.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata
import ch.epfl.sdp.blindwar.data.music.metadata.URIMusicMetadata
import ch.epfl.sdp.blindwar.database.ImageDatabase
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.game.model.GameResult
import ch.epfl.sdp.blindwar.profile.model.AppStatistics
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.StorageReference

class ProfileViewModel: ViewModel() {
    // DATABASE
    private val database = UserDatabase
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser
    //private val userRepository = UserRepository
    private val imageDatabase = ImageDatabase

    // OBSERVABLES
    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val elo = MutableLiveData<String>()
    val imagePath = MutableLiveData<String>()
    val imageRef = MutableLiveData<StorageReference>()
    val userStatistics = MutableLiveData<AppStatistics>()

    private val userInfoListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get User info and use the values to update the UI
            val user: User? = try {
                dataSnapshot.getValue<User>()
            } catch (e: DatabaseException) {
                null
            }

            if (user != null) {
                name.postValue(user.firstName!!)
                email.postValue(user.email)
                elo.postValue(user.userStatistics.elo.toString())
                imagePath.postValue(user.profilePicture)
                userStatistics.postValue(user.userStatistics)

                if (user.profilePicture != "") {
                    imageRef.postValue(imageDatabase.getImageReference(user.profilePicture))
                }

            } else {
                userStatistics.postValue(AppStatistics())
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }

    init {
        if (currentUser != null) {
            database.addUserListener(currentUser.uid, userInfoListener)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun updateStats(score: Int, fails: Int, gameResult: GameResult) {
        if (currentUser != null) {
            UserDatabase.updateSoloUserStatistics(currentUser.uid, score, fails)
            UserDatabase.addGameResult(currentUser.uid, gameResult)
        }
    }

    fun likeMusic(music: URIMusicMetadata) {
        database.addLikedMusic(currentUser?.uid!!, music)
    }
}