package ch.epfl.sdp.blindwar

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainMenuActivity : AppCompatActivity() {
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
        val intent = Intent(this, TutorialActivity::class.java)
        startActivity(intent)
    }

    // Called when the user taps the Solo button
    fun profileButton(view: View) {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    fun launchSpeechRecognitionActivity(view: View) {
        startActivity(Intent(this, VoskActivity::class.java))
    }
}