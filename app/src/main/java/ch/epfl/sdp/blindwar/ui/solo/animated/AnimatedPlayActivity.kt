package ch.epfl.sdp.blindwar.ui.solo.animated

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.ui.solo.GameInstanceViewModel
import ch.epfl.sdp.blindwar.ui.tutorial.GameSummaryFragment
import ch.epfl.sdp.blindwar.ui.tutorial.SongSummaryFragment

class AnimatedPlayActivity: AppCompatActivity() {

    val gameInstanceViewModel: GameInstanceViewModel by viewModels()

    // Add boolean animated configuration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        supportFragmentManager.beginTransaction()
            .replace(R.id.play_container, AnimatedModeSelectionFragment(), "MODE")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    override fun onBackPressed() {

        if (supportFragmentManager.backStackEntryCount > 0) {
            if (supportFragmentManager.fragments.size > 1) {

                if (supportFragmentManager.fragments[1] is SongSummaryFragment) {
                    supportFragmentManager.fragments[0].onResume()
                    supportFragmentManager.popBackStackImmediate()
                }

                else if (supportFragmentManager.fragments[1] is GameSummaryFragment) {
                    removeAllFragments()
                    super.onBackPressed()
                }
            }
        }

        else {
            super.onBackPressed()
        }
    }

    private fun removeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .remove(fragment)
            .commit()
    }

    fun removeAllFragments() {
        for (fragment in supportFragmentManager.fragments) {
            removeFragment(fragment)
        }
    }
}