package ch.epfl.sdp.blindwar.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.BuildConfig
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.game.multi.MultiPlayerMenuActivity
import ch.epfl.sdp.blindwar.menu.MainMenuActivity
import ch.epfl.sdp.blindwar.profile.model.User
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
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

    // See: https://developer.android.com/training/basics/intents/result
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res -> startActivity(onSignInResult(this, res)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        if (!BuildConfig.DEBUG) { // not called when testing
            Firebase.database.setPersistenceEnabled(true)
        }

        if (hasInternet()) {
            checkCurrentUser()
        } else {
            Firebase.auth.signOut() // TODO: Should be done ? /!\ What happens if already sign out ?
            startActivity(Intent(this, MainMenuActivity::class.java))
        }
    }

    private fun checkCurrentUser() {
        Firebase.auth.currentUser?.let {
            // TODO : if not logged in and open the app with a QR code -> show login layout... is it ok >
            Firebase.dynamicLinks
                .getDynamicLink(intent)
                .addOnSuccessListener(this) { pendingDynamicLinkData ->
                    // Get deep link from result (may be null if no link is found)
                    pendingDynamicLinkData?.let {
                        data =
                            pendingDynamicLinkData.link?.getQueryParameter("uid") // what if not connected? uid=UserID?
                    }
                    startActivity(getIntent(data))
                }
                .addOnFailureListener(this) { e ->
                    Log.w(TAG, "getDynamicLink:onFailure", e)
                    startActivity(getIntent(data))
                }
        } ?: run {
            signInLauncher.launch(createSignInIntent())
        }
    }

    private fun createSignInIntent(): Intent {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().setRequireName(false).build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.AnonymousBuilder().build()
        )

        val customLayout = AuthMethodPickerLayout
            .Builder(R.layout.activity_login)
            .setGoogleButtonId(R.id.Btn_google)
            .setEmailButtonId(R.id.Btn_email)
            .setAnonymousButtonId(R.id.Btn_anonymous)
            .build()

        // Create and launch sign-in intent
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setIsSmartLockEnabled(
                !BuildConfig.DEBUG /* credentials */,
                true /* hints */
            ) // -> for TESTs only
            .setAvailableProviders(providers)
            .setLogo(R.drawable.logo)
            .setTheme(R.style.Theme_BlindWar)
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
            // https://www.tabnine.com/code/java/classes/com.google.firebase.auth.FirebaseAuth
            if (response!!.isNewUser) {
                // old check :
                // user?.metadata?.lastSignInTimestamp == user?.metadata?.creationTimestamp
                Firebase.auth.currentUser?.let {
                    val user0 = User.Builder()
                        .setPseudo(it.uid.take(5))
                        .setUid(it.uid)
                        .build()
                    it.email?.let { email ->
                        user0.email = email
                    }
                    UserDatabase.updateUser(
                        user0
                    )
                    Toast.makeText(
                        this,
                        Html.fromHtml("Hi, your pseudo is <b>${user0.pseudo}</b>,<br> you can personalize if in \"Profile\""),
                        Toast.LENGTH_LONG
                    ).show()
                    // TODO: should also initiate a liveData ?
                }
            }
            return setNewUser(data)

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

    private fun hasInternet(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }

    private fun getIntent(data: String?): Intent {
        return data?.let {
            // TODO: check if not connected to a match MainMenuActivity ?
            Intent(this, MultiPlayerMenuActivity::class.java)
                .putExtra(MultiPlayerMenuActivity.DYNAMIC_LINK, data.toString())
        } ?: run {
            Intent(this, MainMenuActivity::class.java)
        }
    }

    private fun setNewUser(data: String?): Intent {
        Firebase.auth.currentUser?.let {
            UserDatabase.setKeepSynced(it.uid)
        }
        return getIntent(data)
    }
}