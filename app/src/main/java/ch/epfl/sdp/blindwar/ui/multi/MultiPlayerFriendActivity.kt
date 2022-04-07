package ch.epfl.sdp.blindwar.ui.multi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R

class MultiPlayerFriendActivity : AppCompatActivity(){

    /**
     * Creates basic layout according to xml
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multifriend)
    }

}
