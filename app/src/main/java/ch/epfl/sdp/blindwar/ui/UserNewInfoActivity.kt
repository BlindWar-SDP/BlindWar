package ch.epfl.sdp.blindwar.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.ImageDatabase
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.user.AppStatistics
import ch.epfl.sdp.blindwar.user.User
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class UserNewInfoActivity : AppCompatActivity() {
    private val database = UserDatabase
    private val imageDatabase = ImageDatabase
    private var profilePictureUri: Uri? = null
    private var isNewUser = false
    private var birthdate: Long = -1
    private var description: String = ""
    private var gender: String = ""


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
                if (!intent.getBooleanExtra(getString(R.string.newUser_ExtraName), false)) {
                    if (it.profilePicture.isNotEmpty()) {
                        imageDatabase.dowloadProfilePicture(
                            it.profilePicture,
                            profileImageView,
                            applicationContext
                        )
                        profilePictureUri = it.profilePicture.toUri()
                    }
                }
                // -> extra for UserAdditionalInfoActivity
                birthdate = it.birthdate
                gender = it.gender
                description = it.description
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_new_info)
    }

    override fun onResume() {
        super.onResume()
        // user id should be set according to authentication
        FirebaseAuth.getInstance().currentUser?.let {
            database.addUserListener(it.uid, userInfoListener)
        }

        isNewUser = intent.getBooleanExtra(getString(R.string.newUser_ExtraName), false)
        if (isNewUser) {
            findViewById<Button>(R.id.NU_deleteProfile).visibility = View.INVISIBLE
            findViewById<Button>(R.id.NU_Cancel_Btn).visibility = View.INVISIBLE
        }
    }

//    override fun onBackPressed() { // TODO: when returning on SplashSreenActivity, not OK...
//        super.onBackPressed()
//        AuthUI.getInstance().signOut(this)
//        AuthUI.getInstance().delete(this)
//    }

    fun confirm(v: View) {
        // basic info
        val pseudo: String = findViewById<EditText>(R.id.NU_pseudo).text.toString()
        val firstName: String = checkNotDefault(
            findViewById<EditText>(R.id.NU_FirstName).text.toString(),
            resources.getString(R.string.first_name)
        )
        val lastName: String = checkNotDefault(
            findViewById<EditText>(R.id.NU_LastName).text.toString(),
            resources.getString(R.string.last_name)
        )
        val profilePicture: String =
            if (profilePictureUri == null) "" else profilePictureUri.toString()

        // additional info
        birthdate = intent.getLongExtra(User.VarName.birthdate.name, -1)
        gender = intent.getStringExtra(User.VarName.gender.name) ?: ""
        description = intent.getStringExtra(User.VarName.description.name) ?: ""

        // check validity of pseudo
        if (pseudo.length < resources.getInteger(R.integer.pseudo_minLength) ||
            pseudo == resources.getString(R.string.text_pseudo)
        ) {
            // Alert Dialog
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            val positiveButtonClick = { _: DialogInterface, _: Int -> }
            builder.setTitle(R.string.new_user_wrong_pseudo_title)
                .setMessage(R.string.new_user_wrong_pseudo_text)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, positiveButtonClick)
            builder.create().show()

        } else {
            // check if new user or update already existing user
            if (isNewUser) {
                createUser(
                    pseudo,
                    firstName,
                    lastName,
                    birthdate,
                    profilePicture,
                    gender,
                    description
                )
                Toast.makeText(
                    this,
                    "Welcome $pseudo 👋", Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this, MainMenuActivity::class.java))
            } else {
                FirebaseAuth.getInstance().currentUser?.let {
                    UserDatabase.setUserString(
                        it.uid, User.VarName.pseudo.name, pseudo
                    )
                    UserDatabase.setUserString(
                        it.uid, User.VarName.firstName.name, firstName
                    )
                    UserDatabase.setUserString(
                        it.uid, User.VarName.lastName.name, lastName
                    )
                    UserDatabase.setUserString(
                        it.uid, User.VarName.profilePicture.name, profilePicture
                    )
                    UserDatabase.setUserString(
                        it.uid, User.VarName.gender.name, gender
                    )
                    UserDatabase.setUserString(
                        it.uid, User.VarName.description.name, description
                    )
                    UserDatabase.setUserLong(
                        it.uid, User.VarName.birthdate.name, birthdate
                    )
                    startActivity(Intent(this, ProfileActivity::class.java))
                } ?: run {
                    Toast.makeText(
                        this,
                        "user's info update went wrong", Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this, SplashScreenActivity::class.java))
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

    fun cancel(v: View) {
        // new Alert Dialogue to ensure deletion
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val positiveButtonClick = { _: DialogInterface, _: Int ->
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        val negativeButtonClick = { _: DialogInterface, _: Int -> }

        builder.setTitle(getString(R.string.alert_dialogue_cancel_title))
            .setMessage(getString(R.string.alert_dialogue_cancel_text))
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok, positiveButtonClick)
            .setNegativeButton(android.R.string.cancel, negativeButtonClick)
            .create()
            .show()
    }

    fun provideMoreInfo(v: View) {
        startActivity(
            Intent(this, UserAdditionalInfoActivity::class.java)
                .putExtra(
                    getString(R.string.newUser_ExtraName),
                    intent.getBooleanExtra(getString(R.string.newUser_ExtraName), false)
                )
                .putExtra(User.VarName.birthdate.name, birthdate)
                .putExtra(User.VarName.description.name, description)
                .putExtra(User.VarName.gender.name, gender)

        )
    }

    fun clearPseudo(v: View) {
        clearText(R.id.NU_pseudo, R.string.text_pseudo)
    }

    fun clearFirstName(v: View) {
        clearText(R.id.NU_FirstName, R.string.first_name)
    }

    fun clearLastName(v: View) {
        clearText(R.id.NU_LastName, R.string.last_name)
    }

    fun choosePicture(v: View) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }

    private fun clearText(id: Int, str: Int) {
        val textView = findViewById<EditText>(id)
        val baseText = getText(str).toString()
        val newText = textView.text.toString()
        if (baseText == newText) {
            textView.text.clear()
        }
    }

    private fun createUser(
        pseudo: String,
        firstName: String,
        lastName: String,
        birthdate: Long,
        profilePicture: String,
        gender: String,
        description: String
    ) {

        val user = Firebase.auth.currentUser
        user?.let {
            UserDatabase.addUser(
                User.Builder(
                    it.uid,
                    it.email!!,
                    AppStatistics(),
                    pseudo,
                    firstName,
                    lastName,
                    birthdate,
                    profilePicture,
                    gender,
                    description
                ).build()
            )
        }
    }

    private fun checkNotDefault(value: String, default: String): String {
        return if (value == default) "" else value
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.let {
                    it.data?.let { uri ->
                        profilePictureUri = uri
                        findViewById<ImageView>(R.id.NU_profileImageView).setImageURI(
                            profilePictureUri
                        )
                    }
                }
            }
        }

    fun deleteProfile(view: View) {
        // Alert Dialog
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val positiveButtonClick = { _: DialogInterface, _: Int ->
            // new Alert Dialogue to ensure deletion
            val builderSecond: AlertDialog.Builder = AlertDialog.Builder(this)
            val secondPositiveButtonClick = { _: DialogInterface, _: Int ->

                FirebaseAuth.getInstance().currentUser?.let {
                    UserDatabase.removeUser(it.uid)
                    AuthUI.getInstance().delete(this).addOnCompleteListener {
                        startActivity(Intent(this, SplashScreenActivity::class.java))
                    }
                    Toast.makeText(
                        this,
                        getString(R.string.deletion_success), Toast.LENGTH_SHORT
                    ).show()
                } ?: run {
                    Toast.makeText(
                        this,
                        "something went wrong on deletion", Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this, SplashScreenActivity::class.java))
                }
            }
            val secondNegativeButtonClick = { _: DialogInterface, _: Int ->
                Toast.makeText(
                    this,
                    getString(R.string.account_not_deleted_confirm_toast) + "🥳", Toast.LENGTH_SHORT
                ).show()
            }

            builderSecond.setTitle(getString(R.string.account_deletion_confirm_title))
                .setMessage(getString(R.string.account_deletion_confirm_text))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, secondPositiveButtonClick)
                .setNegativeButton(android.R.string.cancel, secondNegativeButtonClick)
            builderSecond.create().show()

        }
        val negativeButtonClick = { _: DialogInterface, _: Int ->
            Toast.makeText(
                this,
                getString(R.string.account_not_deleted_toast), Toast.LENGTH_SHORT
            ).show()
        }

        builder.setTitle(getString(R.string.account_deletion_title))
            .setMessage(getString(R.string.account_deletion_text))
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok, positiveButtonClick)
            .setNegativeButton(android.R.string.cancel, negativeButtonClick)
        builder.create().show()
    }
}