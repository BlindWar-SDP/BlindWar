package ch.epfl.sdp.blindwar.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.ui.tutorial.TutorialActivity
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.ui.solo.SoloMenuActivity
import com.firebase.ui.auth.AuthUI

class MainMenuActivity : AppCompatActivity() {

    private val database = UserDatabase()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
    }

    // Called when the user taps the Solo button
    fun soloButton(view: View) {
        val intent = Intent(this, SoloMenuActivity::class.java)
        startActivity(intent)
    }

    // Called when the user taps the Tutorial button
    fun tutorialButton(view: View) {
        database.setElo("JOJO", 1100)
        val intent = Intent(this, TutorialActivity::class.java)
        startActivity(intent)
    }

    // Called when the user taps the Solo button
    fun profileButton(view: View) {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    // Called when the user taps the Solo button
    fun logoutButton(view: View) {
        AuthUI.getInstance().signOut(this)
        AuthUI.getInstance().delete(this) // TODO : for TESTing
//            .addOnCompleteListener {
            startActivity(Intent(this, SplashScreenActivity::class.java))
//        }
    }

    fun launchSpeechRecognitionActivity(view: View) {
        startActivity(Intent(this, VoskActivity::class.java))
    }
}