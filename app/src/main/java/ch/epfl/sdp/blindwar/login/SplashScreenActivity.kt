package ch.epfl.sdp.blindwar.login

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ch.epfl.sdp.blindwar.BuildConfig
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.game.multi.MultiPlayerMenuActivity
import ch.epfl.sdp.blindwar.game.util.NetworkConnectivityChecker.isOnline
import ch.epfl.sdp.blindwar.menu.MainMenuActivity
import ch.epfl.sdp.blindwar.profile.model.User
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase


/**
 * Launcher activity that let the user log/register to the app
 *
 * @constructor creates a SplashScreenActivity
 */
@SuppressLint("CustomSplashScreen")
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
    ) { res -> startActivity(onSignInResult(res)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        if (isOnline()) {
            handleLink()
        } else {
            Firebase.auth.currentUser?.let {
                startActivity(Intent(this, MainMenuActivity::class.java))
            } ?: run {
                val positiveButtonClick = { _: DialogInterface, _: Int ->
                    startActivity(Intent(this, SplashScreenActivity::class.java))
                }
                // Alert Dialog
                AlertDialog.Builder(this).setTitle(getString(R.string.not_logged_in_title))
                    .setMessage(getString(R.string.not_logged_in_msg)).setCancelable(false)
                    .setPositiveButton(android.R.string.ok, positiveButtonClick).create().show()
            }
        }
    }

    /**
     * Handle if there is a dynamic link in the intent or not
     *
     */
    private fun handleLink() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                pendingDynamicLinkData?.let {
                    data =
                        pendingDynamicLinkData.link?.getQueryParameter("uid") // what if not connected? uid=UserID?
                }
                startActivityAfterSplash()
            }.addOnFailureListener {
                startActivityAfterSplash()
            }
    }

    /**
     * Start the next Activity, sign in or menu
     * depending if the user is new or not
     *
     */
    private fun startActivityAfterSplash() {
        Firebase.auth.currentUser?.let {
            startActivity(getIntentData())
        } ?: run {
            signInLauncher.launch(createSignInIntent())
        }
    }

    /**
     * Create the sign in intent for a new user
     *
     * @return
     */
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

    /**
     * When the new user has sign in
     *
     * @param result
     * @return
     */
    private fun onSignInResult(
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
                    UserDatabase.updateUser(user0)
                    Toast.makeText(
                        this,
                        HtmlCompat.fromHtml(
                            "Hi, your pseudo is <b>${user0.pseudo}</b>,<br> you can personalize it in \"Profile\"",
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        ),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            return getIntentData()
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

    /**
     * Create the intent, if a dynamic link has been used,
     * multiplayer menus displayed,
     * else the main menu is displayed
     *
     * @return
     */
    private fun getIntentData(): Intent {
        return data?.let {
            Intent(this, MultiPlayerMenuActivity::class.java)
                .putExtra(MultiPlayerMenuActivity.DYNAMIC_LINK, data.toString())
        } ?: run {
            Intent(this, MainMenuActivity::class.java)
        }
    }
}