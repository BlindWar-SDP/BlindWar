package ch.epfl.sdp.blindwar.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.ui.tutorial.TutorialActivity
import ch.epfl.sdp.blindwar.database.UserDatabase

class MainMenuActivity : AppCompatActivity() {

    private val database = UserDatabase()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
    }

    // Called when the user taps the Solo button
    fun soloButton(view: View) {
        val intent = Intent(this, SoloActivity::class.java)
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
        val intent = Intent(this, LoginActivity::class.java)
        database.setElo("JOJO", 0)
        startActivity(intent)
    }

    fun launchSpeechRecognitionActivity(view: View) {
        startActivity(Intent(this, VoskActivity::class.java))
    }
}