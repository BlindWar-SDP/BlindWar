package ch.epfl.sdp.blindwar.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CalendarView
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
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
//        val profilePicture: Uri? = null

        createUser(pseudo, firstName, lastName, birthDate /*profilePicture*/)
        startActivity(Intent(this, MainMenuActivity::class.java))
    }

    // don't know why, but show message and directly continue to MainMenuActivity
//    private fun checkPseudo(pseudo:String) {
//        Log.i("min pseudo Length", R.integer.pseudo_minLength.toString()) // get 2131296307 instead of 5...
//        if (pseudo.length < 2){ // TODO: replace magic value by value.integers (see above)
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

    private fun createUser(
        pseudo: String,
        firstName: String?,
        lastName: String?,
        birthDate: Long? /*profilePicture: Uri?*/
    ) {
//        checkPseudo(pseudo)
        val user = Firebase.auth.currentUser
        user?.let {
            /*UserDatabase.addUser(*/User.Builder(
            user.email!!,
            AppStatistics(),
            pseudo,
            firstName,
            lastName,
            birthDate /*profilePicture*/
        ).build() /*)*/
        }
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
}