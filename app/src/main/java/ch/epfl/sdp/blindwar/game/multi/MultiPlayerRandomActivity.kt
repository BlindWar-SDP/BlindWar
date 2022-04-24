package ch.epfl.sdp.blindwar.game.multi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R

/**
 * Activity that lets a user play a multiplayer game with a friend
 *
 * @constructor creates a MultiPlayerFriendActivity
 */
class MultiPlayerRandomActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multirandom)
    }
}
