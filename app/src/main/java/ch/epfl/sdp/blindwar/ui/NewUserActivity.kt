package ch.epfl.sdp.blindwar.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.user.AppStatistics
import ch.epfl.sdp.blindwar.user.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*


class NewUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

    }
    companion object{
        private var birthDate0: Long? = null
        private const val minAge = 5
        private const val maxAge = 100
    }

    fun confirm(view: View){
        val pseudo: String = findViewById<EditText>(R.id.NU_pseudo).text.toString()
        val firstName: String? = findViewById<EditText>(R.id.NU_FirstName).text.toString()
        val lastName: String? = findViewById<EditText>(R.id.NU_LastName).text.toString()
        val birthDate: Long? = birthDate0
//        var profilePicture: Uri? = null

        createUser(pseudo, firstName, lastName, birthDate /*profilePicture*/)
        startActivity(Intent(this, MainMenuActivity::class.java))
    }

    fun selectBirthdate(view: View) {
        val calendar: Calendar = Calendar.getInstance() // current date
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        calendar.add(Calendar.YEAR, -minAge)
        val year = calendar.get(Calendar.YEAR)
        val datePickerDialog =
            DatePickerDialog(this, { _, mYear, mDay, mMonth -> setDate(mYear, mMonth+1, mDay) }, year, month,day)
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        calendar.add(Calendar.YEAR, -maxAge)
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.show()
    }


    fun clearPseudo(view:View) {
        clearText(R.id.NU_pseudo, R.string.text_pseudo)
    }
    fun clearFirstName(view:View) {
        clearText(R.id.NU_FirstName, R.string.first_name)
    }
    fun clearLastName(view:View) {
        clearText(R.id.NU_LastName, R.string.last_name)
    }

    private fun clearText(id:Int, str:Int){
        val textView = findViewById<EditText>(id)
        val baseText = getText(str).toString()
        val newText = textView.text.toString()
        if (baseText == newText){
            textView.text.clear()
        }
    }
    private fun createUser(
        pseudo: String,
        firstName: String?,
        lastName: String?,
        birthDate: Long? /*profilePicture: Uri?*/
    ) {
        // set default value to null:

//        checkPseudo(pseudo)
        val user = Firebase.auth.currentUser
        user?.let {
            UserDatabase().addUser(
                user.uid,
                User.Builder(
                    user.email!!,
                    AppStatistics(),
                    pseudo,
                    checkNotDefault(firstName, R.string.first_name),
                    checkNotDefault(lastName, R.string.last_name),
                    birthDate /*profilePicture*/
                ).build())
        }
    }

    private fun checkNotDefault(value: String?, default:Int): String?{
        return  if (value == default.toString()) null else value
    }

    private fun setDate(year:Int, month:Int, day:Int) {
        val cal: Calendar = Calendar.getInstance()
        cal.set(year, month, day)
        birthDate0 = cal.timeInMillis
    }
}