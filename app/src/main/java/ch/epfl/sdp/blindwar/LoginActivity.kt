package ch.epfl.sdp.blindwar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ch.epfl.sdp.blindwar.database.UserDatabase

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    fun loginButton(view: View) {
        val database = UserDatabase()
        val appStatistics = AppStatistics()
        val user = User("Jojo", "Joestar", "@", "JOJO",
            appStatistics)
        database.addUser(user)
        startActivity(Intent(this, MainMenuActivity::class.java))
    }
}