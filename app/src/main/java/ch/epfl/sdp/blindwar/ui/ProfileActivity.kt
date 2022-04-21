package ch.epfl.sdp.blindwar.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.ImageDatabase
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.user.User
import ch.epfl.sdp.blindwar.user.UserCache
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.gson.Gson

class ProfileActivity : AppCompatActivity(), UserCache {
    private val database = UserDatabase
    private val imageDatabase = ImageDatabase

    private var user = User()

    private fun downloadImage() {
        if (user.profilePicture.isNotEmpty()) { // not default value
            imageDatabase.dowloadProfilePicture(
                user.profilePicture,
                findViewById(R.id.profileImageView),
                applicationContext
            )
        }
    }

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
            }
            setView()
            downloadImage()
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val offline = getSharedPreferences("offline", MODE_PRIVATE)
            .getBoolean("offline", false)
        if (offline) {
            val offlineStr = getSharedPreferences("offline", MODE_PRIVATE)
                .getString("user", null)
            offlineStr?.let {
                user = Gson().fromJson(it, User::class.java)
            }
        } else {
            // user id should be set according to authentication
            FirebaseAuth.getInstance().currentUser?.let {
                database.addUserListener(it.uid, userInfoListener)
            }
        }
        setContentView(R.layout.activity_profile)

        // showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        setView()
    }

    fun editProfile(view: View) {
        startActivity(Intent(this, UserNewInfoActivity::class.java))
    }

    fun logoutButton(view: View) {
        // TODO : add warning for offline logout (lost of userinfo update)
        // TODO : same on backpressed on MainMenuActivity ?
        FirebaseAuth.getInstance().signOut()
        removeCache(this)
        startActivity(Intent(this, SplashScreenActivity::class.java))
    }

    fun statisticsButton(view: View) {
        startActivity(Intent(this, StatisticsActivity::class.java))
    }

    private fun setView() {
        findViewById<TextView>(R.id.nameView).text = user.firstName
        findViewById<TextView>(R.id.emailView).text = user.email
        findViewById<TextView>(R.id.eloDeclarationView).text =
            user.userStatistics.elo.toString()
    }
}