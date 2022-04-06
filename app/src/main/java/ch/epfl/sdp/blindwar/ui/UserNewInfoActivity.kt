package ch.epfl.sdp.blindwar.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
import java.util.*


class UserNewInfoActivity : AppCompatActivity() {
    private val database = UserDatabase
    private val imageDatabase = ImageDatabase
    private val currentUser = FirebaseAuth.getInstance().currentUser

    private var birthDate0: Long = -1
    private var profilePicture0: Uri? = null
    private var minAge = -1
    private var maxAge = -1

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
            user?.let{
                firstName.setText(it.firstName)
                lastName.setText(it.lastName)
                pseudo.setText(it.pseudo)
                it.profilePicture?.let{ pp ->
                    if (!intent.getBooleanExtra("newUser", false) && pp != "null") {
                        imageDatabase.dowloadProfilePicture(
                            pp,
                            profileImageView,
                            applicationContext
                        )
                    }
                }
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

        minAge = resources.getInteger(R.integer.age_min)
        maxAge = resources.getInteger(R.integer.age_max)

        // user id should be set according to authentication
        currentUser?.let{
            database.addUserListener(it.uid, userInfoListener)
        }
    }

//    override fun onBackPressed() { // TODO: when returning on SplashSreenActivity, not OK...
//        super.onBackPressed()
//        AuthUI.getInstance().signOut(this)
//        AuthUI.getInstance().delete(this)
//    }

    fun confirm(view: View) {
        val pseudo: String = findViewById<EditText>(R.id.NU_pseudo).text.toString()
        val firstName: String? = checkNotDefault(findViewById<EditText>(R.id.NU_FirstName).text.toString(), R.string.first_name)
        val lastName: String? = checkNotDefault(findViewById<EditText>(R.id.NU_LastName).text.toString(), R.string.last_name)
        val birthDate: Long = birthDate0
        var profilePicture: Uri? = profilePicture0

        // check validity of pseudo
        if (pseudo.length < resources.getInteger(R.integer.pseudo_minLength) || pseudo == resources.getString(
                R.string.text_pseudo
            )
        ) {
            // Alert Dialog
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            val positiveButtonClick = { _: DialogInterface, _: Int ->
                Toast.makeText(
                    this,
                    android.R.string.ok, Toast.LENGTH_SHORT
                ).show()
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
            if (intent.getBooleanExtra("newUser", false)) {
                createUser(
                    pseudo,
                    firstName,
                    lastName,
                    birthDate,
                    profilePicture.toString()
                ) // TODO : Comment for TESTing -> need to uncomment
//            AuthUI.getInstance().delete(this) // TODO : uncomment for TESTing
            } else {
                val uid = currentUser?.uid!!
                UserDatabase.setPseudo(uid, pseudo)
                firstName?.let{UserDatabase.setFirstName(uid, it)}
                lastName?.let{UserDatabase.setLastName(uid, it)}
                profilePicture?.let{UserDatabase.setProfilePicture(uid, it.toString())}
            }

            // Upload picture to database
            profilePicture?.let {
                imageDatabase.uploadProfilePicture(
                    currentUser, it,
                    findViewById(android.R.id.content)
                )
            }
            startActivity(Intent(this, MainMenuActivity::class.java))
        }
    }

    fun selectBirthdate(view: View) {
        val calendar: Calendar = Calendar.getInstance() // current date
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        calendar.add(Calendar.YEAR, -minAge)
        val year = calendar.get(Calendar.YEAR)
        val datePickerDialog =
            DatePickerDialog(
                this,
                { _, mYear, mDay, mMonth -> setDate(mYear, mMonth + 1, mDay) },
                year,
                month,
                day
            )
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        calendar.add(Calendar.YEAR, -maxAge)
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.setIcon(R.drawable.logo);
        datePickerDialog.setTitle(R.string.new_user_birthdatePicker)
        datePickerDialog.show()
    }


    fun clearPseudo(v: View) {
        clearText(R.id.NU_pseudo, R.string.text_pseudo)
    }

    fun clearFirstName(view: View) {
        clearText(R.id.NU_FirstName, R.string.first_name)
    }

    fun clearLastName(view: View) {
        clearText(R.id.NU_LastName, R.string.last_name)
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
        firstName: String?,
        lastName: String?,
        birthDate: Long?,
        profilePicture: String?
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
                    birthDate,
                    profilePicture
                ).build()
            )
        }
    }

    private fun checkNotDefault(value: String?, default: Int): String? {
        return if (value == default.toString()) null else value
    }

    private fun setDate(year: Int, month: Int, day: Int) {
        val cal: Calendar = Calendar.getInstance()
        cal.set(year, month, day)
        birthDate0 = cal.timeInMillis
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    if (data.data != null) {
                        profilePicture0 = data.data
                        findViewById<ImageView>(R.id.NU_profileImageView).setImageURI(
                            profilePicture0
                        )
                    }
                }
            }
        }

    fun choosePicture(view: View) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }
}