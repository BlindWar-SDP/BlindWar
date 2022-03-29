package ch.epfl.sdp.blindwar.ui

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.ImageDatabase
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.user.User
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ProfileActivity : AppCompatActivity() {
    private val database = UserDatabase
    private val imageDatabase = ImageDatabase
    private val currentUser = FirebaseAuth.getInstance().currentUser

    private val userInfoListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get User info and use the values to update the UI
            val user = dataSnapshot.getValue<User>()
            val nameView = findViewById<TextView>(R.id.nameView)
            val emailView = findViewById<TextView>(R.id.emailView)
            val eloView = findViewById<TextView>(R.id.eloDeclarationView)
            val profileImageView = findViewById<ImageView>(R.id.profileImageView)
            val view = findViewById<View>(R.id.content)
            if (user != null) {
                nameView.text = user.firstName
                emailView.text = user.email
                eloView.text = user.userStatistics.elo.toString()

                val imagePath = user.profilePicture
                if (imagePath != null) {
                    imageDatabase.dowloadProfilePicture(imagePath, profileImageView, view)
                }
            }

        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }


    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                if (data.data != null) {
                    val profilePic = findViewById<ImageView>(R.id.profileImageView)
                    val view = findViewById<View>(R.id.content)
                    //profilePic!!.setImageURI(data.data)

                    // Upload picture to database
                    val imagePath = imageDatabase.uploadImage(
                        currentUser!!.uid,
                        data.data!!, findViewById(android.R.id.content))

                    imageDatabase.dowloadProfilePicture(imagePath, profilePic, view)

                    // Update user profilePic
                    currentUser?.let {
                        database.addProfilePicture(it.uid, imagePath) }
                }
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // user id should be set according to authentication
        if (currentUser != null) {
            database.addUserListener(currentUser.uid, userInfoListener)
        }
        setContentView(R.layout.activity_profile)

    }


    fun choosePicture(view: View) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }


    fun logoutButton(view: View) {
        AuthUI.getInstance().signOut(this).addOnCompleteListener {
            startActivity(Intent(this, SplashScreenActivity::class.java))
        }

    }


    fun statisticsButton(view: View) {
        startActivity(Intent(this, StatisticsActivity::class.java))
    }

}