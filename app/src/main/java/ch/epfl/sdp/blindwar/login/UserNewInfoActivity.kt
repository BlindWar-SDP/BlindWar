package ch.epfl.sdp.blindwar.login

//import ch.epfl.sdp.blindwar.user.UserCache
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
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.ImageDatabase
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.menu.MainMenuActivity
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
import java.util.*


/**
 * Activity that let the user enter its principal information when registering for the app
 * TODO: fix CodeClimate issues / fix Cirrus warnings when possible
 *
 * @constructor creates a UserNewInfoActivity
 */
class UserNewInfoActivity : AppCompatActivity()/*, UserCache*/ {
    private val database = UserDatabase
    private val imageDatabase = ImageDatabase

    // global variable -> not a good practice
    private var minAge = -1
    private var maxAge = -1
    private var localPPuri: Uri? = null
    private var user = User()

    /**
     * Makes sure data is ok, before launching
     */
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

    /**
     * Listener for user entering new information
     */
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

            if (user.pseudo.isEmpty()) { // 1st login
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

    /**
     * Generates the layout and adds listener for current user
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_new_info)

        // cannot be initialized to resources value outside
        minAge = resources.getInteger(R.integer.age_min)
        maxAge = resources.getInteger(R.integer.age_max)

//        if (isOffline(this)) {
//            disableButton(R.id.NU_deleteProfile)
//            disableButton(R.id.NU_editProfilePicture)
//            disableButton(R.id.NU_deleteProfile)
//            hideResetPPBtn()
//            user = readCache(this)
//            setFromBundle()
//            setView()
//        } else {
        FirebaseAuth.getInstance().currentUser?.let {
//            if ( ! it.isAnonymous) {
//            disableButton(R.id.NU_selected_provider)
//            disableButton(R.id.NU_Btn_google)
//            disableButton(R.id.NU_Btn_email)
//            }

            database.addUserListener(it.uid, userInfoListener)
        }
//        }
    }

//    fun linkGoogle(v: View){
//        updateProvider(GoogleAuthProvider.getCredential("", null))
//    }
//
//    fun linkEmail(v: View){
//        updateProvider(EmailAuthProvider.getCredential("", ""))
//    }

    /**
     * To avoid crashing the app, if the back button is pressed, user will log out
     *
     */
    override fun onBackPressed() {
        //this.moveTaskToBack(true)
        val intent = intent
        val activity = intent.getStringExtra("activity")
        //Log.w("yeet", activity!!)
        if (activity != "profile") {
            AuthUI.getInstance().delete(this)
            FirebaseAuth.getInstance().signOut()
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
        assert(v.id == R.id.NU_Confirm_Btn)
        // basic info
        setFromText()

        // check validity of pseudo
        if (user.pseudo.length < resources.getInteger(R.integer.pseudo_minLength)) {
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
//            writeCache(this, user)
            updateUser()
        }
    }

    fun cancel(v: View) {
        assert(v.id == R.id.NU_Cancel_Btn)

        // new Alert Dialogue to ensure deletion
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val positiveButtonClick = { _: DialogInterface, _: Int ->
            startActivity(
                Intent(
                    this,
                    MainMenuActivity::class.java
                )
            ) // ?? was profile activity before it becomes a fragment
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

    /**
     * Lets the user choose their own profile picture
     *
     * @param v
     */
    fun choosePicture(v: View) {
        assert(v.id == R.id.NU_editProfilePicture)

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }

    fun resetProfilePicture(v: View) {
        assert(v.id == R.id.NU_resetProfilePicture)

        user.profilePicture = User().profilePicture // set to default
        localPPuri = null
        hideResetPPBtn()
        showImage()
    }

    fun selectBirthdate(v: View) {
        assert(v.id == R.id.NU_select_birthdate)

        val calendar: Calendar = Calendar.getInstance() // current date
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        calendar.add(Calendar.YEAR, -minAge)
        val year = calendar.get(Calendar.YEAR)
        val datePickerDialog =
            DatePickerDialog(
                this,
                { _, mYear, mMonth, mDay -> setDate(mYear, mMonth, mDay) },
                year,
                month,
                day
            )
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        calendar.add(Calendar.YEAR, -maxAge)
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.setIcon(R.drawable.logo)
        datePickerDialog.setTitle(R.string.new_user_birthdatePicker)
        datePickerDialog.show()
    }

    fun resetBirthdate(v: View) {
        assert(v.id == R.id.NU_reset_birthdate)

        user.birthdate = resources.getInteger(R.integer.default_birthdate).toLong()
        val textView = findViewById<TextView>(R.id.NU_selected_birthdate_text)
        textView.text = resources.getString(R.string.no_birthdate_selected)
        findViewById<Button>(R.id.NU_reset_birthdate).visibility = View.INVISIBLE
    }

    /**
     * Handle the profile deletion logic
     */
    fun deleteProfile(v: View) {
        assert(v.id == R.id.NU_deleteProfile)

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
//                    removeCache(this)
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

    /**
     * Internal function for checking if string is empty
     *
     * @param value
     * @param default
     * @return
     */
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
        user.description = findViewById<EditText>(R.id.NU_description).text.toString()
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
        Firebase.auth.currentUser?.let { auth ->
            val user0 = User.Builder()
                .fromUser(user)
                .setUid(auth.uid)
                .build()
            auth.email?.let { email ->
                user0.email = email
            }
            UserDatabase.updateUser(
                user0
            )
            Toast.makeText(
                this,
                "${user.pseudo}'s info updated", Toast.LENGTH_SHORT
            ).show()

        } ?: run {
            Toast.makeText(
                this,
                "update local data only",
                Toast.LENGTH_LONG
            ).show()
        }
        startActivity(Intent(this, MainMenuActivity::class.java))
    }

    private fun setView() {
        findViewById<EditText>(R.id.NU_pseudo).setText(user.pseudo)
        findViewById<EditText>(R.id.NU_FirstName).setText(user.firstName)
        findViewById<EditText>(R.id.NU_LastName).setText(user.lastName)
        showImage()
        if (user.birthdate != resources.getInteger(R.integer.default_birthdate).toLong()) {
            setBirthdateText(user.birthdate)
            findViewById<Button>(R.id.NU_reset_birthdate).visibility = View.VISIBLE
        } else {
            findViewById<Button>(R.id.NU_reset_birthdate).visibility = View.INVISIBLE
        }
        findViewById<TextView>(R.id.NU_description).text = user.description


        // Spinner for GENDER

        // access the spinner
        val spinner = findViewById<Spinner>(R.id.gender_spinner)
        spinner?.let {
            val adapter = ArrayAdapter(
                this,
                R.layout.spinner_item,
                User.Gender.values()
            )

//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            it.adapter = adapter
            if (user.gender.isNotEmpty()) {
                // because default value for gender is "", which is not in the Enum Class
                it.setSelection(User.Gender.valueOf(user.gender).ordinal)
            } else {
                it.setSelection(adapter.count - 1)
            }
            it.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    val chosen = parent.getItemAtPosition(position)
                    user.gender = if (chosen == User.Gender.None) {
                        ""
                    } else {
                        chosen.toString()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    user.gender = ""
                }
            }
        }
    }

    private fun disableButton(id: Int) {
//        findViewById<Button>(id).isClickable = false
        findViewById<Button>(id).visibility = View.INVISIBLE
    }

    private fun setDate(year: Int, month: Int, day: Int) {
        val cal: Calendar = Calendar.getInstance()
        cal.set(year, month, day)
        user.birthdate = cal.timeInMillis
        setBirthdateText(user.birthdate)

    }

    private fun setBirthdateText(birthdate: Long) {
        val cal: Calendar = Calendar.getInstance()
        cal.timeInMillis = birthdate

        val textView = findViewById<TextView>(R.id.NU_selected_birthdate_text)
        val textStr = "birthdate set to\n" +
                "${cal.get(Calendar.DAY_OF_MONTH)}/" +
                "${cal.get(Calendar.MONTH) + 1}/" +
                "${cal.get(Calendar.YEAR)}"
        textView.text = textStr
        findViewById<Button>(R.id.NU_reset_birthdate).visibility = View.VISIBLE
    }

//    private fun updateProvider(credential: AuthCredential){
//        Toast.makeText(this, "Get HERE.",
//            Toast.LENGTH_SHORT).show()
//        Firebase.auth.currentUser!!.linkWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    Log.d(TAG, "linkWithCredential:success")
//                    Toast.makeText(this, "Get HERE.",
//                        Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this, SplashScreenActivity::class.java))
//                } else {
//                    Log.w(TAG, "linkWithCredential:failure", task.exception)
//                    Toast.makeText(baseContext, "Authentication failed.",
//                        Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
}



// +++++++++++++++++++++++++++++++++++++
// lines for activity_user_new_info.xml if link anonymous to auth provider
// +++++++++++++++++++++++++++++++++++++
/*
<TextView
android:id="@+id/NU_selected_provider"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:layout_marginStart="@dimen/NU_left_margin"
android:layout_marginTop="@dimen/NU_between_field_margin"
app:layout_constraintStart_toStartOf="parent"
app:layout_constraintTop_toBottomOf="@id/NU_description"
android:text="link anonymous account with: "/>

<Button
android:id="@+id/NU_Btn_google"
style="@style/Widget.Material3.Button.UnelevatedButton"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:backgroundTint="@color/fui_transparent"
android:drawableTop="@drawable/common_google_signin_btn_icon_light_focused"
android:text="@string/Btn_google"
android:textAllCaps="false"
android:textAppearance="@style/Widget.Material3.ExtendedFloatingActionButton.Icon.Primary"
android:textColor="@color/ivory"
android:textSize="10sp"
android:onClick="linkGoogle"
android:layout_marginStart="@dimen/NU_left_margin"
app:layout_constraintStart_toEndOf="@id/NU_selected_provider"
app:layout_constraintEnd_toStartOf="@+id/NU_Btn_email"
app:layout_constraintTop_toTopOf="@id/NU_selected_provider"
app:layout_constraintBottom_toBottomOf="@+id/NU_selected_provider"/>

<Button
android:id="@+id/NU_Btn_email"
style="@style/Widget.Material3.Button.UnelevatedButton"
android:layout_width="wrap_content"
android:layout_height="0dp"
android:backgroundTint="@color/fui_transparent"
android:drawableTop="@android:drawable/ic_dialog_email"
android:text="@string/Btn_email"
android:textAppearance="@style/Widget.Material3.ExtendedFloatingActionButton.Icon.Primary"
android:textColor="@color/ivory"
android:textSize="10sp"
android:onClick="linkEmail"
app:layout_constraintStart_toEndOf="@+id/NU_Btn_google"
app:layout_constraintEnd_toEndOf="parent"
app:layout_constraintTop_toTopOf="@id/NU_Btn_google"
app:layout_constraintBottom_toBottomOf="@id/NU_Btn_google"/>
*/
