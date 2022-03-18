package ch.epfl.sdp.blindwar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.EditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class NewUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

    }

    fun confirm(view: View){
//        val pseudo: String? = findViewById<EditText>(R.id.NU_pseudo).text.toString()
//        val firstName: String? = findViewById<EditText>(R.id.NU_FirstName).text.toString()
//        val lastName: String? = findViewById<EditText>(R.id.NU_LastName).text.toString()
//        val birthDate: Long? = findViewById<CalendarView>(R.id.NU_birthdate).date

//        createUser(pseudo, firstName, lastName, birthDate)
        startActivity(Intent(this, MainMenuActivity::class.java))
    }

//    private fun createUser(pseudo: String?, firstName: String?, lastName: String?, birthDate: Long?) {
//        val user = Firebase.auth.currentUser
//        user?.let {
//            // Name, email address, and profile photo Url
////            val name = user.displayName
////            val email = user.email
////            val photoUrl = user.photoUrl
//
//            // Check if user's email is verified
////            val emailVerified = user.isEmailVerified
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getToken() instead.
////            val uid = user.uid
//            User.Builder(user.email!!, AppStatistics(), pseudo, firstName, lastName, birthDate, null).build().registerUser()
//        }
//    }
}