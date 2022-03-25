package ch.epfl.sdp.blindwar.ui

import android.app.AlertDialog
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


class NewUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

    }

    fun confirm(view: View){
        val pseudo: String = findViewById<EditText>(R.id.NU_pseudo).text.toString()
        val firstName: String? = findViewById<EditText>(R.id.NU_FirstName).text.toString()
        val lastName: String? = findViewById<EditText>(R.id.NU_LastName).text.toString()
        val birthDate: Long? = findViewById<CalendarView>(R.id.NU_calendar).date
//        var profilePicture: Uri? = null

        createUser(pseudo, firstName, lastName, birthDate /*profilePicture*/)
        startActivity(Intent(this, MainMenuActivity::class.java))
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
}