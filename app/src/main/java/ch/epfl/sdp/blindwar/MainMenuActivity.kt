package ch.epfl.sdp.blindwar


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        findViewById<Button>(R.id.logoutButton).setOnClickListener{
            // Firebase logout
            FirebaseAuth.getInstance().signOut()
            // Google logout
            GoogleSignIn.getClient(this,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            ).signOut()

            startActivity(Intent(this, MainActivity::class.java)) // TODO : can be directly LoginActivity
            finish()
        }
    }
}

