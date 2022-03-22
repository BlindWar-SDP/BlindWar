package ch.epfl.sdp.blindwar.ui

import android.content.Intent
import ch.epfl.sdp.blindwar.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ch.epfl.sdp.blindwar.BuildConfig
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
    // See: https://developer.android.com/training/basics/intents/result
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res -> this.onSignInResult(res) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            startActivity(Intent(this, MainMenuActivity::class.java))
        } else {
            createSignInIntent()
        }
    }

    private fun createSignInIntent() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().setRequireName(false).build(),
            AuthUI.IdpConfig.GoogleBuilder().build())
//            AuthUI.IdpConfig.AnonymousBuilder().build())

        val customLayout = AuthMethodPickerLayout
            .Builder(R.layout.activity_login)
            .setGoogleButtonId(R.id.Btn_google)
            .setEmailButtonId(R.id.Btn_email)
            .build()

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */, true /* hints */) // -> for TESTs only
            .setAvailableProviders(providers)
            .setLogo(R.drawable.logo) // still used if setAuthMethodPickerLayout(customLayout) ?
            .setTheme(R.style.Theme_BlindWar) // Set theme
            .setAuthMethodPickerLayout(customLayout)
            .build()

        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            Log.i("lastSignin", "OK")
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser // =?= Firebase.auth.currentUser
            // https://www.tabnine.com/code/java/classes/com.google.firebase.auth.FirebaseAuth

            if( user?.metadata?.lastSignInTimestamp == user?.metadata?.creationTimestamp) {
                // new user: 1st signIn
                startActivity(Intent(this, NewUserActivity::class.java))
                finish()
            } else {
                /*
            - should we update the online database with the local cache here ?
             */
                startActivity(Intent(this, MainMenuActivity::class.java))
                finish()
            }
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            // Sign in failed
            if (response == null) {
                // User pressed back button
//                showSnackbar(R.string.sign_in_cancelled);
                return
            }
            if (response.error?.errorCode == ErrorCodes.NO_NETWORK) {
//                showSnackbar(R.string.no_internet_connection);
                return
            }
//            showSnackbar(R.string.unknown_error);
            Log.e(TAG, "Sign-in error: ", response.error)
        }
    }

    /* To be defined in the User class ??
    private fun signOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                startActivity(Intent(this, SplashScreenActivity::class.java))
                finish()
            }
    }

    private fun delete() {
        AuthUI.getInstance()
            .delete(this)
            .addOnCompleteListener {
                startActivity(Intent(this, SplashScreenActivity::class.java))
                finish()
            }
    }
     */

}