package ch.epfl.sdp.blindwar.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.ui.multi.MultiPlayerActivity
import ch.epfl.sdp.blindwar.ui.solo.PlayActivity
import ch.epfl.sdp.blindwar.ui.tutorial.TutorialActivity
import ch.epfl.sdp.blindwar.user.UserCache
import com.google.firebase.auth.FirebaseAuth

class MainMenuActivity : AppCompatActivity(), UserCache {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        if(isOffline(this)){
            findViewById<Button>(R.id.multiButton).isClickable = false
            findViewById<Button>(R.id.multiButton).alpha = 0.3F
        }
    }

    // Called when the user taps the Solo button
    fun soloButton(view: View) {
        val intent = Intent(this, PlayActivity::class.java)
        startActivity(intent)
    }

    fun multiButton(view: View) {
        val intent = Intent(this, MultiPlayerActivity::class.java)
        startActivity(intent)
    }

    // Called when the user taps the Tutorial button
    fun tutorialButton(view: View) {
        FirebaseAuth.getInstance().currentUser?.let {
            UserDatabase.setElo(it.uid, 1000)
        }
        val intent = Intent(this, TutorialActivity::class.java)
        startActivity(intent)
    }

    // Called when the user taps the Solo button
    fun profileButton(view: View) {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    fun launchSpeechRecognitionActivity(view: View) {
        startActivity(Intent(this, DemoSRActivity::class.java))
    }

    override fun onBackPressed() {
        FirebaseAuth.getInstance().signOut()
        removeCache(this)
        val intent = Intent(this, SplashScreenActivity::class.java)
        startActivity(intent)
    }
}