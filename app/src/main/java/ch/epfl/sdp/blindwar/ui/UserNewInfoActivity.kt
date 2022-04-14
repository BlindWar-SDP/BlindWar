package ch.epfl.sdp.blindwar.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
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
import okhttp3.internal.wait


class UserNewInfoActivity : AppCompatActivity() {
    private val database = UserDatabase
    private val imageDatabase = ImageDatabase

    private var user = User()

    private fun isNewUser(): Boolean {
        return user.pseudo.isEmpty()
    }

    private val userInfoListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get User info and use the values to update the UI
            val userDB: User? = try {
                dataSnapshot.getValue<User>()
            } catch (e: DatabaseException) {
                null
            }
            userDB?.let {
                user = User.Builder().fromUser(it).build()
//                if (user.profilePicture.isNotEmpty()) {
//                    profilePictureUri = user.profilePicture.toUri()
//                }
            }
            if (isNewUser()) {
                findViewById<Button>(R.id.NU_deleteProfile).visibility = View.INVISIBLE
                findViewById<Button>(R.id.NU_Cancel_Btn).visibility = View.INVISIBLE
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.let {
                    it.data?.let { uri ->
                        user.profilePicture = uri.toString()
                        showImage()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_new_info)
        FirebaseAuth.getInstance().currentUser?.let {
            database.addUserListener(it.uid, userInfoListener)//.wait()
        }
        setInfoFromBundle()
        downloadImage()
        setView()

    }

//    override fun onPause() {
//        super.onPause()
//        setInfoFromText()
//    }

//    override fun onBackPressed() { // TODO: when returning on SplashSreenActivity, not OK...
//        super.onBackPressed()
//        AuthUI.getInstance().signOut(this)
//        AuthUI.getInstance().delete(this)
//    }

    fun confirm(v: View) {
        // Additional info
        setInfoFromBundle()
        // basic info
        setInfoFromText()

        // check validity of pseudo
        if (user.pseudo.length < resources.getInteger(R.integer.pseudo_minLength) ||
            user.pseudo == resources.getString(R.string.text_pseudo)
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
            updateUser()
            uploadImage()
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
        setInfoFromText()
        startActivity(
            Intent(this, UserAdditionalInfoActivity::class.java)
                .putExtras(createBundle())
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

    fun deleteProfile(v: View) {
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


    // =============================================================================================
    // ================================== PRIVATE FUNCTIONS ========================================
    // =============================================================================================
    private fun checkNotDefault(value: String, default: String): String {
        return if (value == default) "" else value
    }

    private fun setInfoFromText() {

        user.pseudo = findViewById<EditText>(R.id.NU_pseudo).text.toString()
        user.firstName = checkNotDefault(
            findViewById<EditText>(R.id.NU_FirstName).text.toString(),
            resources.getString(R.string.first_name)
        )
        user.lastName = checkNotDefault(
            findViewById<EditText>(R.id.NU_LastName).text.toString(),
            resources.getString(R.string.last_name)
        )
    }

    private fun setInfoFromBundle() {
        intent.extras?.let {
            user.pseudo = it.getString(User.VarName.pseudo.name, user.pseudo)
            user.firstName = it.getString(User.VarName.firstName.name, user.firstName)
            user.lastName = it.getString(User.VarName.lastName.name, user.lastName)
            user.profilePicture =
                it.getString(User.VarName.profilePicture.name, user.profilePicture)
            user.gender = it.getString(User.VarName.gender.name, user.gender)
            user.description = it.getString(User.VarName.description.name, user.description)
            user.birthdate = it.getLong(User.VarName.birthdate.name, user.birthdate)
        }
    }

    private fun clearText(id: Int, str: Int) {
        val textView = findViewById<EditText>(id)
        val baseText = getText(str).toString()
        val newText = textView.text.toString()
        if (baseText == newText) {
            textView.text.clear()
        }
    }

    private fun showImage() {
        Log.i("profile String", user.profilePicture)
        findViewById<ImageView>(R.id.NU_profileImageView).setImageURI(
            user.profilePicture.toUri()
        )
    }

    private fun uploadImage() {
        // Upload picture to database
        if (user.profilePicture.isNotEmpty()) {
            imageDatabase.uploadProfilePicture(
                FirebaseAuth.getInstance().currentUser,
                user.profilePicture.toUri(),
                findViewById(android.R.id.content)
            )
        }
    }

    private fun downloadImage() {
        intent.extras?.let {} ?: run {
            if (user.profilePicture.isNotEmpty()) { // not default value
                imageDatabase.dowloadProfilePicture(
                    user.profilePicture,
                    findViewById(R.id.NU_profileImageView),
                    applicationContext
                )
            }
        }
    }

    private fun updateUser() {
        Firebase.auth.currentUser?.let {
            if (user.pseudo.isEmpty()) {
                UserDatabase.addUser(
                    User.Builder()
                        .fromUser(user)
                        .setUid(it.uid)
                        .setEmail(it.email!!)
                        .setStats(AppStatistics())
                        .build()
                )
                Toast.makeText(
                    this,
                    "Welcome ${user.pseudo} 👋", Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this, MainMenuActivity::class.java))
            } else {
                UserDatabase.updateUser(user)
                Toast.makeText(
                    this,
                    "${user.pseudo}'s info updated 👋", Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this, ProfileActivity::class.java))
            }
        } ?: run {
            Toast.makeText(
                this,
                "user's update went wrong", Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this, SplashScreenActivity::class.java))
        }
    }

    private fun createBundle(
    ): Bundle {
        val bundle = Bundle()
        bundle.putString(User.VarName.pseudo.name, user.pseudo)
        bundle.putString(User.VarName.firstName.name, user.firstName)
        bundle.putString(User.VarName.lastName.name, user.lastName)
        bundle.putString(User.VarName.profilePicture.name, user.profilePicture)
        bundle.putLong(User.VarName.birthdate.name, user.birthdate)
        bundle.putString(User.VarName.gender.name, user.gender)
        bundle.putString(User.VarName.description.name, user.description)
        bundle.putBoolean(
            getString(R.string.newUser_ExtraName),
            intent.getBooleanExtra(getString(R.string.newUser_ExtraName), false)
        )
        return bundle
    }

    private fun setView() {
        // set the view
        findViewById<EditText>(R.id.NU_pseudo).setText(user.pseudo)
        findViewById<EditText>(R.id.NU_FirstName).setText(user.firstName)
        findViewById<EditText>(R.id.NU_LastName).setText(user.lastName)
        showImage()
    }
}