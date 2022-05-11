package ch.epfl.sdp.blindwar.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.BuildConfig
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.multi.MultiPlayerMenuActivity
import ch.epfl.sdp.blindwar.menu.MainMenuActivity
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase

/**
 * Launcher activity that let the user log/register to the app
 * TODO: CodeClimate / Cirrus warnings
 *
 * @constructor creates a SplashScreenActivity
 */
class SplashScreenActivity : AppCompatActivity() {
    // inspired by :
    // https://github.com/firebase/snippets-android/blob/master/auth/app/src/main/java/com/google/firebase/quickstart/auth/kotlin/FirebaseUIActivity.kt
    // https://firebase.google.com/docs/auth/android/firebaseui

    companion object {
        private const val TAG = "SplashScreen"
    }

    private var data: String? = null
//    private var userAuth = UserAuth()

    // See: https://developer.android.com/training/basics/intents/result
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res -> startActivity(onSignInResult(this, res)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                if (pendingDynamicLinkData != null) {
                    data = pendingDynamicLinkData.link?.getQueryParameter("uid")
                }
            }
            .addOnFailureListener(this) { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }
        checkCurrentUser()
    }

//    override fun onResume() {
//        super.onResume()
//        checkCurrentUser()
//    }


    private fun checkCurrentUser() {
        if (isSignedIn()) {
            if (data == null)
                startActivity(Intent(this, MainMenuActivity::class.java))
            else {
                val intent = Intent(this, MultiPlayerMenuActivity::class.java)
                intent.putExtra(MultiPlayerMenuActivity.DYNAMIC_LINK, data)
                startActivity(intent)
            }
        } else {
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
            val user = FirebaseAuth.getInstance().currentUser // =?= Firebase.auth.currentUser
            // https://www.tabnine.com/code/java/classes/com.google.firebase.auth.FirebaseAuth

            return if (user?.metadata?.lastSignInTimestamp == user?.metadata?.creationTimestamp) {
                // new user: 1st signIn
                if (data != null) {
                    Intent(activity, UserNewInfoActivity::class.java).putExtra("newUser", true)
                        .putExtra(
                            MultiPlayerMenuActivity.DYNAMIC_LINK,
                            data.toString()
                        )
                } else {
                    Intent(activity, UserNewInfoActivity::class.java).putExtra("newUser", true)
                }
            } else {
                /*
                    - should we update the online database with the local cache here ?
                     */
                if (data == null)
                    Intent(activity, MainMenuActivity::class.java)
                else {
                    Intent(this, MultiPlayerMenuActivity::class.java).putExtra(
                        MultiPlayerMenuActivity.DYNAMIC_LINK,
                        data.toString()
                    )
                }

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

    private fun isSignedIn(): Boolean {
        return Firebase.auth.currentUser != null
    }
}