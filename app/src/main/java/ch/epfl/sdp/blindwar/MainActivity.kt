package ch.epfl.sdp.blindwar

import android.content.Intent
import android.os.Bundle
//import android.os.Handler
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

const val EXTRA_MESSAGE = "ch.epfl.blindwar.MESSAGE"

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
//        mAuth.signOut()
        val user = mAuth.currentUser

//        Handler().postDelayed({
            // if already logged in: start MainMenu
            if (user != null) {
                startActivity(Intent(this, MainMenuActivity::class.java))
                finish()
                // if not logged in: start LoginActivity
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
//       }, 5_000)
    }

    /** Called when the user taps the Send button */
    fun sendMessage(view: View) {
        val editText = findViewById<EditText>(R.id.editTextTextPersonName)
        val message = editText.text.toString()
        val intent = Intent(this, GreetingActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }
}