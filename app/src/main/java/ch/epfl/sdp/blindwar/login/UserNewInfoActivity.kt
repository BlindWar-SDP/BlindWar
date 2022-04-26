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
import ch.epfl.sdp.blindwar.menu.MainMenuActivity
import ch.epfl.sdp.blindwar.profile.model.AppStatistics
import ch.epfl.sdp.blindwar.profile.model.User
import ch.epfl.sdp.blindwar.user.UserCache
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * Activity that let the user enter its principal information when registering for the app
 * TODO: add KDOC / fix CodeClimate issues / fix Cirrus warnings when possible
 *
 * @constructor creates a UserNewInfoActivity
 */
class UserNewInfoActivity : AppCompatActivity(), UserCache {
    private val database = UserDatabase
    private val imageDatabase = ImageDatabase

    // global variable -> not a good practice
    private var user = User()
    private var isNewUser = false
    private var localPPuri: Uri? = null

    private fun hideCancelDeleteBtn() {
        disableButton(R.id.NU_deleteProfile)
        disableButton(R.id.NU_Cancel_Btn)
    }

    private fun hideResetPPBtn() {
        findViewById<ImageView>(R.id.NU_profileImageView).setImageResource(
            android.R.color.transparent
        )
        disableButton(R.id.NU_resetProfilePicture)
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
            }
            // check for first login
            intent.extras?.let {
                setFromBundle()
            } ?: run {
                isNewUser = user.pseudo.isEmpty()
            }
            if (isNewUser) { // 1st login
                hideCancelDeleteBtn()
            }
            if (user.profilePicture.isEmpty()) {
                hideResetPPBtn()
            }
            setView()
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_new_info)

        if (isOffline(this)) {
            disableButton(R.id.NU_deleteProfile)
            disableButton(R.id.NU_editProfilePicture)
            disableButton(R.id.NU_deleteProfile)
            hideResetPPBtn()
            user = readCache(this)
            setFromBundle()
            setView()
        } else {
            FirebaseAuth.getInstance().currentUser?.let {
                database.addUserListener(it.uid, userInfoListener)
            }
        }
    }

    fun confirm(v: View) {
        // Additional info
        setFromBundle()
        // basic info
        setFromText()

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
            uploadImage()
            writeCache(this, user)
            updateUser()
        }
    }

    fun resetProfilePicture(v: View) {
        user.profilePicture = User().profilePicture // set to default
        localPPuri = null
        hideResetPPBtn()
        showImage()
    }

    fun cancel(v: View) {
        // new Alert Dialogue to ensure deletion
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val positiveButtonClick = { _: DialogInterface, _: Int ->
            startActivity(Intent(this, MainMenuActivity::class.java)) // ?? was profile activity before it becomes a fragment
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
        setFromText()
        val bundle = Bundle()
        bundle.putSerializable(
            User.VarName.user.name,
            Json.encodeToString(User.serializer(), user)
        )
        localPPuri?.let {
            bundle.putString("localPP", localPPuri.toString())
        }
        startActivity(
            Intent(this, UserAdditionalInfoActivity::class.java)
                .putExtras(bundle)
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

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.let {
                    it.data?.let { uri ->
                        localPPuri = uri
                        showImage()
                    }
                }
            }
        }

    fun choosePicture(v: View) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }

    /**
     * Handle the profile deletion logic
     */
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
                    getString(R.string.account_not_deleted_confirm_toast) + "🥳",
                    Toast.LENGTH_SHORT
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

    private fun setFromText() {
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

    private fun setFromBundle() {
        intent.extras?.let { bundle ->
            val serializable = bundle.getString(User.VarName.user.name)
            serializable?.let { userStr ->
                user = Json.decodeFromString(userStr)
            }
            isNewUser =
                bundle.getBoolean(resources.getString(R.string.newUser_ExtraName), false)
            bundle.getString("localPP")?.let { str ->
                localPPuri = str.toUri()
            }
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
        localPPuri?.let {
            findViewById<ImageView>(R.id.NU_profileImageView).setImageURI(
                localPPuri
            )
            findViewById<Button>(R.id.NU_resetProfilePicture).visibility = View.VISIBLE
        } ?: run {
            downloadImage()
        }
    }

    private fun uploadImage() {
        localPPuri?.let {
            // Upload picture to database
            user.profilePicture =
                imageDatabase.uploadProfilePicture(
                    FirebaseAuth.getInstance().currentUser,
                    it,
                    findViewById(android.R.id.content)
                )

            Toast.makeText(
                this,
                "uploading profile picture", Toast.LENGTH_SHORT
            ).show()

            // time to load photo, otherwise, not on server when loading ProfileActivity
            // bad practice... need to improve this with ProgressDialog ?
            Thread.sleep(1000)
        }
    }

    private fun downloadImage() {
        if (user.profilePicture.isNotEmpty()) { // not default value
            imageDatabase.downloadProfilePicture(
                user.profilePicture,
                findViewById(R.id.NU_profileImageView),
                applicationContext
            )
        }
    }

    private fun updateUser() {
        Firebase.auth.currentUser?.let {
            if (isNewUser) { // firstLog()
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
                finish()
            } else {
                UserDatabase.updateUser(user)
                Toast.makeText(
                    this,
                    "${user.pseudo}'s info updated 👋", Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this, MainMenuActivity::class.java)) // was ProfileActivity
                finish()
            }
        } ?: run {
            Toast.makeText(
                this,
                "user's local info updated", Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this, MainMenuActivity::class.java)) // was ProfileActivity
        }
    }

    private fun setView() {
        // set the view
        findViewById<EditText>(R.id.NU_pseudo).setText(user.pseudo)
        findViewById<EditText>(R.id.NU_FirstName).setText(user.firstName)
        findViewById<EditText>(R.id.NU_LastName).setText(user.lastName)
        showImage()
    }

    private fun disableButton(id: Int) {
//        findViewById<Button>(id).isClickable = false
        findViewById<Button>(id).visibility = View.INVISIBLE
    }
}