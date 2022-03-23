package ch.epfl.sdp.blindwar.ui

import android.content.Intent
import ch.epfl.sdp.blindwar.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ch.epfl.sdp.blindwar.BuildConfig
import ch.epfl.sdp.blindwar.user.UserAuth
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashScreenActivity : AppCompatActivity() {
    // inspired by :
    // https://github.com/firebase/snippets-android/blob/master/auth/app/src/main/java/com/google/firebase/quickstart/auth/kotlin/FirebaseUIActivity.kt
    // https://firebase.google.com/docs/auth/android/firebaseui

    companion object {
        private const val TAG = "SplashScreen"
    }

    private var userAuth = UserAuth()

    // See: https://developer.android.com/training/basics/intents/result
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res -> startActivity(userAuth.onSignInResult(this, res))}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        if (userAuth.isSignedIn()) {
            startActivity(Intent(this, MainMenuActivity::class.java))
        } else {
            signInLauncher.launch(userAuth.createSignInIntent())
        }
    }

}