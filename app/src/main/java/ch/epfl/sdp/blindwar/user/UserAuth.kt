package ch.epfl.sdp.blindwar.user

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.BuildConfig
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.ui.MainMenuActivity
import ch.epfl.sdp.blindwar.ui.NewUserActivity
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserAuth {
    companion object {
        private const val TAG = "UserAuth"
    }

    fun createSignInIntent(): Intent{
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
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */, true /* hints */) // -> for TESTs only
            .setAvailableProviders(providers)
            .setLogo(R.drawable.logo) // still used if setAuthMethodPickerLayout(customLayout) ?
            .setTheme(R.style.Theme_BlindWar) // Set theme
            .setAuthMethodPickerLayout(customLayout)
            .build()
    }

    fun onSignInResult(activity: Activity, result: FirebaseAuthUIAuthenticationResult): Intent? {
        val response = result.idpResponse
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            Log.i("lastSignin", "OK")
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser // =?= Firebase.auth.currentUser
            // https://www.tabnine.com/code/java/classes/com.google.firebase.auth.FirebaseAuth

            if( user?.metadata?.lastSignInTimestamp == user?.metadata?.creationTimestamp) {
                // new user: 1st signIn
                return Intent(activity, NewUserActivity::class.java)
            } else {
                /*
            - should we update the online database with the local cache here ?
             */
                return Intent(activity, MainMenuActivity::class.java)
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
            Log.e(UserAuth.TAG, "Sign-in error: ", response.error)
            return null
        }
    }

    fun isSignedIn(): Boolean {
        return Firebase.auth.currentUser != null
    }

    fun signOut(context: Context) {
        AuthUI.getInstance()
            .signOut(context)
    }

    //    fun delete(context: Context) {
//        AuthUI.getInstance()
//            .delete(context)
//    }

    fun createUser(
        pseudo: String,
        firstName: String?,
        lastName: String?,
        birthDate: Long? /*profilePicture: Uri?*/
    ) {
        // set default value to null:

//        checkPseudo(pseudo)
        val user = Firebase.auth.currentUser
        user?.let {
            UserDatabase().addUser(User.Builder(
                user.email!!,
                AppStatistics(),
                pseudo,
                checkNotDefault(firstName, R.string.first_name),
                checkNotDefault(lastName, R.string.last_name),
                birthDate /*profilePicture*/
            ).build())
        }
    }

    // =================
    // Private Functions
    // =================
    private fun checkNotDefault(value: String?, default:Int): String?{
        return  if (value == default.toString()) null else value
    }

    // don't know why, but show message and directly continue to MainMenuActivity
//    private fun checkPseudo(pseudo:String) {
//        Log.i("min pseudo Length", R.integer.pseudo_minLength.toString()) // get 2131296307 instead of 5...
//        if (pseudo.length < 2 || pseudo == R.string.text_pseudo.toString()){ // TODO: replace magic value by value.integers (see above)
//            Log.i("too short pseudo Length", pseudo.length.toString())
//            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
//            val positiveButtonClick = { dialog: DialogInterface, which: Int ->
//                Toast.makeText(applicationContext,
//                    android.R.string.ok, Toast.LENGTH_SHORT).show()
//            }
//
//            builder.setTitle("Profile Creation Alert")
//                .setMessage("Please, enter a valid Pseudo")
//                .setCancelable(false)
//                .setPositiveButton(android.R.string.ok, positiveButtonClick)
//            builder.create().show()
//        }
//
//    }

}