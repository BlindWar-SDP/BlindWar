package ch.epfl.sdp.blindwar.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.user.AppStatistics
import ch.epfl.sdp.blindwar.user.User

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    fun loginButton(view: View) {
        val database = UserDatabase()
        val appStatistics = AppStatistics()
        val user = User("Jojo@BlindWar.ch", appStatistics,"JoestAAr", "jojo", "star", 123123213, /*null*/)
        database.addUser(user)
        startActivity(Intent(this, NewUserActivity::class.java))
    }
}
