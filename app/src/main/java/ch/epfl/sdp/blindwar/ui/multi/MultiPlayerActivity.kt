package ch.epfl.sdp.blindwar.ui.multi

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.ui.ProfileActivity

class MultiPlayerActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi)
    }

    fun friendButton(view: View) {
        val intent = Intent(this, MultiPlayerFriendActivity::class.java)
        startActivity(intent)
    }

    fun randomButton(view: View) {
        val intent = Intent(this, MultiPlayerRandomActivity::class.java)
        startActivity(intent)
    }

}
