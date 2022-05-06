package ch.epfl.sdp.blindwar.login.viewmodel

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.blindwar.database.ImageDatabase
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import java.util.*

class UserNewInfoViewModel : ViewModel() {

    // OBSERVABLES
    val userMLD = MutableLiveData<User>()
    var user = User()
    val imageRef = MutableLiveData<StorageReference?>()

    private val userInfoListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get User info and use the values to update the UI
            val userDB: User? = try {
                dataSnapshot.getValue<User>()
            } catch (e: DatabaseException) {
                null
            }
            userDB?.let {
                user = it
                if (it.profilePicture.isNotEmpty()) {
                    imageRef.postValue(ImageDatabase.getImageReference(it.profilePicture))
                }
            }
            userMLD.postValue(user)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }

    init {
        Firebase.auth.currentUser?.let {
            UserDatabase.addUserListener(it.uid, userInfoListener)
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }

    fun uploadImage(uri: Uri?) {
        uri?.let { it_uri ->
            // Upload picture to database
            user.profilePicture = ImageDatabase.uploadProfilePicture(it_uri)
//            Thread.sleep(1000)
            // time to load photo, otherwise, not on server when loading ProfileActivity
            // bad practice... need to improve this with ProgressDialog ?
            userMLD.postValue(user)
            imageRef.postValue(ImageDatabase.getImageReference(user.profilePicture))
        }
    }

    fun setMLDFromText(pseudo: String, firstName: String, lastName: String, description: String) {
        user.pseudo = pseudo
        user.firstName = firstName
        user.lastName = lastName
        user.description = description
        userMLD.postValue(user)
    }

    fun resetPicture() {
        user.profilePicture = User().profilePicture
        imageRef.postValue(null)
        userMLD.postValue(user)
    }

    fun resetBirthdate() {
        user.birthdate = User().birthdate
        userMLD.postValue(user)
    }

    fun setDate(year: Int, month: Int, day: Int): Long {
        val cal: Calendar = Calendar.getInstance()
        cal.set(year, month, day)
        user.birthdate = cal.timeInMillis
        userMLD.postValue(user)
        return cal.timeInMillis
    }
}