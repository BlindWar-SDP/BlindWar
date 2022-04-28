package ch.epfl.sdp.blindwar.ui.multi

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.multi.MultiPlayerFriendActivity
import ch.epfl.sdp.blindwar.game.multi.MultiPlayerRandomActivity

class MultiPlayerMenuActivity : AppCompatActivity() {

    /**
     * Creates basic layout according to xml
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiplayer_menu)

        // showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Button logic towards the multiplayer with friends activity
     *
     * @param view
     */
    fun friendButton(view: View) {
        //val intent = Intent(this, MultiPlayerFriendActivity::class.java)
        val intent = Intent(this, MultiPlayerFriendActivity::class.java)
        startActivity(intent)
    }

    /**
     * Button logic towards the multiplayer with random person activity
     *
     * @param view
     */
    fun randomButton(view: View) {
        val intent = Intent(this, MultiPlayerRandomActivity::class.java)
        startActivity(intent)
    }

}
