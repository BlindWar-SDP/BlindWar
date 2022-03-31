package ch.epfl.sdp.blindwar.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.user.AppStatistics
import ch.epfl.sdp.blindwar.user.User
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*


class NewUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)
    }

    override fun onBackPressed() { // TODO: when returning on SplashSreenActivity, not OK...
        super.onBackPressed()
        AuthUI.getInstance().signOut(this)
        AuthUI.getInstance().delete(this)
    }

    private var birthDate0: Long = 0
    private val minAge = 5
    private val maxAge = 100

    fun confirm(view: View) {
        val pseudo: String = findViewById<EditText>(R.id.NU_pseudo).text.toString()
        val firstName: String = findViewById<EditText>(R.id.NU_FirstName).text.toString()
        val lastName: String = findViewById<EditText>(R.id.NU_LastName).text.toString()
        val birthDate: Long = birthDate0
        var profilePicture: String? = null

        // check validity of pseudo
        if (pseudo.length < resources.getInteger(R.integer.pseudo_minLength) || pseudo == resources.getString(
                R.string.text_pseudo
            )
        ) {

            // Alert Dialog
//            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
//            val positiveButtonClick = { _: DialogInterface, _: Int ->
//                Toast.makeText(
//                    this,
//                    android.R.string.ok, Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            builder.setTitle(R.string.new_user_wrong_pseudo_title)
//                .setMessage(R.string.new_user_wrong_pseudo_text)
//                .setCancelable(false)
//                .setPositiveButton(android.R.string.ok, positiveButtonClick)
//            builder.create().show()

            // Or Toast
            Toast.makeText(this, R.string.new_user_wrong_pseudo_text, Toast.LENGTH_SHORT).show()

        } else {
//            createUser(pseudo, firstName, lastName, birthDate profilePicture) // TODO : Comment for TESTing -> need to uncomment
            AuthUI.getInstance().delete(this) // TODO : for TESTing -> need to delete line
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
        // set default value to null:

        val user = Firebase.auth.currentUser
        user?.let {
            UserDatabase.addUser(
                user.uid,
                User.Builder(
                    user.email!!,
                    AppStatistics(),
                    pseudo,
                    checkNotDefault(firstName, R.string.first_name),
                    checkNotDefault(lastName, R.string.last_name),
                    birthDate,
                    profilePicture
                ).build())
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
}