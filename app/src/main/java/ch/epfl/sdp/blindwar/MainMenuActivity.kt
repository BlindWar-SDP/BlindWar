package ch.epfl.sdp.blindwar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        // Sign Out TODO : chose between [SIGN/LOG] -> button/functions coherence
        findViewById<Button>(R.id.logoutButton).setOnClickListener{
            signOut()
        }
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

    // Called when the user taps the Sign Out button
    fun signOut(){
        // Firebase logout
        FirebaseAuth.getInstance().signOut()
        // Google logout
        GoogleSignIn.getClient(this,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        ).signOut()

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
