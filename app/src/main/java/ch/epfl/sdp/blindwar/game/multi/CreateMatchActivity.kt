package ch.epfl.sdp.blindwar.game.multi

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.model.config.GameFormat
import ch.epfl.sdp.blindwar.game.solo.fragments.GameSummaryFragment
import ch.epfl.sdp.blindwar.game.solo.fragments.ModeSelectionFragment
import ch.epfl.sdp.blindwar.game.solo.fragments.SongSummaryFragment
import ch.epfl.sdp.blindwar.game.util.GameActivity

class CreateMatchActivity: GameActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        // Set the game format
        gameInstanceViewModel.setGameFormat(GameFormat.MULTI)

        supportFragmentManager.beginTransaction()
            .replace(R.id.play_container, ModeSelectionFragment(), "MODE")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    /**
     * Handle the back button logic in the activity
     */
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            if (supportFragmentManager.fragments.size > 1) {

                if (supportFragmentManager.fragments[1] is SongSummaryFragment) {
                    supportFragmentManager.fragments[0].onResume()
                    supportFragmentManager.popBackStackImmediate()
                } else if (supportFragmentManager.fragments[1] is GameSummaryFragment) {
                    removeAllFragments()
                    super.onBackPressed()
                }
            }
        } else {
            super.onBackPressed()
        }
    }
}
/*
package ch.epfl.sdp.blindwar.game.multi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R

class CreateMatchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_match)

        val nb: NumberPicker = findViewById(R.id.numberOfPLayers)
        nb.maxValue = 20
        nb.minValue = 2
        nb.value = 2
    }

    fun createMatchSoloAttributes(view: View) {
        val nb: NumberPicker = findViewById(R.id.numberOfPLayers)
        val checkBox: CheckBox = findViewById(R.id.checkBoxIsPrivate)

        val maxPlayer = nb.value
        val isPrivate = checkBox.isChecked
    }

    /**
     * cancel the creation
     *
     * @param view
     */
    fun cancel(view: View) {
        startActivity(Intent(this, MultiPlayerMenuActivity::class.java))
    }
}
 */