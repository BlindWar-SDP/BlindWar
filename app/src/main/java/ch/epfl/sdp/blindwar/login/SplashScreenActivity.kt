package ch.epfl.sdp.blindwar.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.BuildConfig
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.menu.MainMenuActivity
import ch.epfl.sdp.blindwar.user.UserCache
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

/**
 * Launcher activity that let the user log/register to the app
 * TODO: CodeClimate / Cirrus warnings
 *
 * @constructor creates a SplashScreenActivity
 */
class SplashScreenActivity : AppCompatActivity(), UserCache {
    // inspired by :
    // https://github.com/firebase/snippets-android/blob/master/auth/app/src/main/java/com/google/firebase/quickstart/auth/kotlin/FirebaseUIActivity.kt
    // https://firebase.google.com/docs/auth/android/firebaseui

    companion object {
        private const val TAG = "SplashScreen"
    }

    // See: https://developer.android.com/training/basics/intents/result
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res -> startActivity(onSignInResult(this, res)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        if (isOnline()) {
            setOffline(this, false)
            checkCurrentUser()
        } else {
            setOffline(this, true)
            createCache(this)
            Toast.makeText(
                this,
                "OFFLINE gameplay", Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this, MainMenuActivity::class.java))
        }
    }

    private fun isOnline(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }

    private fun checkCurrentUser() {
        FirebaseAuth.getInstance().currentUser?.let {
            // upload local data
            updateServerFromCache(this, it)
            startActivity(Intent(this, MainMenuActivity::class.java))
        } ?: run {
            signInLauncher.launch(createSignInIntent())
        }
    }

    private fun createSignInIntent(): Intent {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().setRequireName(false).build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
//            AuthUI.IdpConfig.AnonymousBuilder().build())

        val customLayout = AuthMethodPickerLayout
            .Builder(R.layout.activity_login)
            .setGoogleButtonId(R.id.Btn_google)
            .setEmailButtonId(R.id.Btn_email)
            .build()

        // Create and launch sign-in intent
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setIsSmartLockEnabled(
                !BuildConfig.DEBUG /* credentials */,
                true /* hints */
            ) // -> for TESTs only
            .setAvailableProviders(providers)
            .setLogo(R.drawable.logo) // still used if setAuthMethodPickerLayout(customLayout) ?
            .setTheme(R.style.Theme_BlindWar) // Set theme
            .setAuthMethodPickerLayout(customLayout)
            .build()
    }

    private fun onSignInResult(
        activity: Activity,
        result: FirebaseAuthUIAuthenticationResult
    ): Intent? {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            FirebaseAuth.getInstance().currentUser?.let { user ->// =?= Firebase.auth.currentUser
                // https://www.tabnine.com/code/java/classes/com.google.firebase.auth.FirebaseAuth
                return if (user.metadata?.lastSignInTimestamp == user.metadata?.creationTimestamp) {
                    // new user: 1st signIn
                    updateServerFromCache(this, user)
                    Intent(activity, UserNewInfoActivity::class.java)
                } else {
                    // user already known (as logged out: no local data (deleted on logout))
                    Intent(activity, MainMenuActivity::class.java)
                }
            } ?: run {
                Toast.makeText(
                    this,
                    "something went wrong on login", Toast.LENGTH_SHORT
                ).show()
                return Intent(activity, SplashScreenActivity::class.java)
//                return null // ?? better option ??
            }
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            // Sign in failed
            if (response == null) {
                // User pressed back button
                return null
            }
            if (response.error?.errorCode == ErrorCodes.NO_NETWORK) {
                return null
            }
            Log.e(TAG, "Sign-in error: ", response.error)
            return null
        }
    }
}