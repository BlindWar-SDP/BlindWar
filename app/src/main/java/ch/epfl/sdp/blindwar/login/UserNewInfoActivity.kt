package ch.epfl.sdp.blindwar.login

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.ImageDatabase
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.menu.MainMenuActivity
import ch.epfl.sdp.blindwar.profile.model.AppStatistics
import ch.epfl.sdp.blindwar.profile.model.Gender
import ch.epfl.sdp.blindwar.profile.model.User
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

/**
 * Activity that let the user enter its principal information when registering for the app
 *
 * @constructor creates a UserNewInfoActivity
 */
class UserNewInfoActivity : AppCompatActivity() {
    private val database = UserDatabase
    private val imageDatabase = ImageDatabase
    private var profilePictureUri: Uri? = null
    private val auth = FirebaseAuth.getInstance()

    /**
     * Listener for user entering new information
     */
    private val userInfoListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get User info and use the values to update the UI
            val user: User? = try {
                dataSnapshot.getValue<User>()
            } catch (e: DatabaseException) {
                null
            }
            val firstName = findViewById<EditText>(R.id.NU_FirstName)
            val lastName = findViewById<EditText>(R.id.NU_LastName)
            val pseudo = findViewById<EditText>(R.id.NU_pseudo)
            val profileImageView = findViewById<ImageView>(R.id.NU_profileImageView)
            user?.let {
                firstName.setText(it.firstName)
                lastName.setText(it.lastName)
                pseudo.setText(it.pseudo)
                if (!intent.getBooleanExtra("newUser", false)) {
                    if (it.profilePicture != "null") {
                        // TODO: Refactor UserNewInfoActivity
                        //imageDatabase.downloadProfilePicture(
                          //  it.profilePicture!!,
                           // profileImageView,
                           // applicationContext
                        //)
                    }
                }
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }

    /**
     * Generates the layout and adds listener for current user
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_new_info)

        // user id should be set according to authentication
        FirebaseAuth.getInstance().currentUser?.let {
            database.addUserListener(it.uid, userInfoListener)
        }
    }

    /**
     * To avoid crashing the app, if the back button is pressed, user will log out
     *
     */
    override fun onBackPressed() {
        //this.moveTaskToBack(true);
        val intent = intent
        val activity = intent.getStringExtra("activity")
        //Log.w("yeet", activity!!)
        if (activity != "profile") {
            AuthUI.getInstance().delete(this)
            auth.signOut()
            startActivity(Intent(this, SplashScreenActivity::class.java))
        } else {
            super.onBackPressed()
        }
    }

    /**
     * Function for confirming and saving all info entered by user
     *
     * @param v
     */
    fun confirm(v: View) {
        val pseudo: String = findViewById<EditText>(R.id.NU_pseudo).text.toString()
        val firstName: String = checkNotDefault(
            findViewById<EditText>(R.id.NU_FirstName).text.toString(),
            R.string.first_name
        )
        val lastName: String = checkNotDefault(
            findViewById<EditText>(R.id.NU_LastName).text.toString(),
            R.string.last_name
        )
        val birthDate: Long = intent.getLongExtra("birthdate", -1)
        val profilePicture: String = profilePictureUri.toString()
        val gender = intent.getStringExtra("gender") ?: Gender.None.toString()
        val description = intent.getStringExtra("description") ?: ""
        val isNewUser = intent.getBooleanExtra("newUser", false)

        // check validity of pseudo
        if (pseudo.length < resources.getInteger(R.integer.pseudo_minLength) || pseudo == resources.getString(
                R.string.text_pseudo
            )
        ) {
            // Alert Dialog
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            val positiveButtonClick = { _: DialogInterface, _: Int ->
            }

            builder.setTitle(R.string.new_user_wrong_pseudo_title)
                .setMessage(R.string.new_user_wrong_pseudo_text)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, positiveButtonClick)
            builder.create().show()

            // Or Toast
//            Toast.makeText(this, R.string.new_user_wrong_pseudo_text, Toast.LENGTH_SHORT).show()

        } else {
            // check if new user or update already existing user
            if (isNewUser) {
                createUser(
                    pseudo,
                    firstName,
                    lastName,
                    birthDate,
                    profilePicture.toString(),
                    gender,
                    description
                ) // TODO : Comment for TESTing -> need to uncomment
//            AuthUI.getInstance().delete(this) // TODO : uncomment for TESTing
                startActivity(Intent(this, MainMenuActivity::class.java))
            } else {
                FirebaseAuth.getInstance().currentUser?.let {
                    UserDatabase.setPseudo(it.uid, pseudo)
                    UserDatabase.setFirstName(it.uid, firstName)
                    UserDatabase.setLastName(it.uid, lastName)
                    UserDatabase.setProfilePicture(it.uid, profilePicture)
                    UserDatabase.setGender(it.uid, gender)
                    UserDatabase.setBirthdate(it.uid, birthDate)
                    UserDatabase.setDescription(it.uid, description)
                    startActivity(Intent(this, MainMenuActivity::class.java))
                }
            }

            // Upload picture to database
            profilePictureUri?.let {
                imageDatabase.uploadProfilePicture(
                    FirebaseAuth.getInstance().currentUser, it,
                    findViewById(android.R.id.content)
                )
            }
        }
    }

    /**
     * Provides the user with opportunity to add more info (gender, description, birthdate)
     *
     * @param v
     */
    fun provideMoreInfo(v: View) {
        startActivity(
            Intent(this, UserAdditionalInfoActivity::class.java)
                .putExtra("newUser", intent.getBooleanExtra("newUser", false))
        )
    }

    /**
     * Clears the user's pseudo
     *
     * @param v
     */
    fun clearPseudo(v: View) {
        clearText(R.id.NU_pseudo, R.string.text_pseudo)
    }

    /**
     * Clears the user's first name
     *
     * @param v
     */
    fun clearFirstName(v: View) {
        clearText(R.id.NU_FirstName, R.string.first_name)
    }

    /**
     * Clears the user's last name
     *
     * @param v
     */
    fun clearLastName(v: View) {
        clearText(R.id.NU_LastName, R.string.last_name)
    }

    /**
     * Lets the user choose their own profile picture
     *
     * @param v
     */
    fun choosePicture(v: View) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }

    /**
     * Clears text
     *
     * @param id
     * @param str
     */
    private fun clearText(id: Int, str: Int) {
        val textView = findViewById<EditText>(id)
        val baseText = getText(str).toString()
        val newText = textView.text.toString()
        if (baseText == newText) {
            textView.text.clear()
        }
    }

    /**
     * Creates the user in the Firebase database
     *
     * @param pseudo
     * @param firstName
     * @param lastName
     * @param birthDate
     * @param profilePicture
     * @param gender
     * @param description
     */
    private fun createUser(
        pseudo: String,
        firstName: String?,
        lastName: String?,
        birthDate: Long?,
        profilePicture: String,
        gender: String,
        description: String?
    ) {

        val user = Firebase.auth.currentUser
        user?.let {
            UserDatabase.updateUser(
                User.Builder(
                    it.uid,
                    it.email!!,
                    AppStatistics(),
                    pseudo,
                    firstName!!,
                    lastName!!,
                    birthDate!!,
                    profilePicture,
                    gender = gender,
                    description = description!!
                ).build()
            )
        }
    }

    /**
     * Internal function for checking if string is empty
     *
     * @param value
     * @param default
     * @return
     */
    private fun checkNotDefault(value: String, default: Int): String {
        return if (value == default.toString()) "" else value
    }

    /**
     * Makes sure data is ok, before launching
     */
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    if (data.data != null) {
                        profilePictureUri = data.data
                        findViewById<ImageView>(R.id.NU_profileImageView).setImageURI(
                            profilePictureUri
                        )
                    }
                }
            }
        }
}

