package ch.epfl.sdp.blindwar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

const val EXTRA_MESSAGE = "ch.epfl.blindwar.MESSAGE"

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // TODO : adapt the activity interface

        mAuth = FirebaseAuth.getInstance()
//        mAuth.signOut()
        val user = mAuth.currentUser

        // if already logged in: start MainMenu
        if (user != null) {
            startActivity(Intent(this, MainMenuActivity::class.java))
            finish()

        // if not logged in: start LoginActivity
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}