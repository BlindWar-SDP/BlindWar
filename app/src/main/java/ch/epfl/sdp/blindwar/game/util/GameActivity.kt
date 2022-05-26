package ch.epfl.sdp.blindwar.game.util

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.model.config.GameFormat
import ch.epfl.sdp.blindwar.game.solo.fragments.GameSummaryFragment
import ch.epfl.sdp.blindwar.game.solo.fragments.ModeSelectionFragment
import ch.epfl.sdp.blindwar.game.solo.fragments.SongSummaryFragment
import ch.epfl.sdp.blindwar.game.viewmodels.GameInstanceViewModel
import ch.epfl.sdp.blindwar.profile.viewmodel.ProfileViewModel

class GameActivity : AppCompatActivity() {
    val gameInstanceViewModel: GameInstanceViewModel by viewModels()
    protected val profileViewModel: ProfileViewModel by viewModels()

    companion object {
        /* Used to handle permission request */
        const val PERMISSIONS_REQUEST_RECORD_AUDIO = 1

        const val GAME_FORMAT_EXTRA_NAME = "GAME_FORMAT"
        const val GAME_IS_PRIVATE = "IS_PRIVATE"
        const val GAME_MAX_PLAYERS = "MAX_PLAYERS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        // Set the game format
        val format: GameFormat = intent.extras!!.get(GAME_FORMAT_EXTRA_NAME) as GameFormat
        gameInstanceViewModel.setGameFormat(format)
        if (format == GameFormat.MULTI) {
            gameInstanceViewModel.setMultiParameters(
                intent.extras!!.get(GAME_IS_PRIVATE) as Boolean, intent.extras!!.get(
                    GAME_MAX_PLAYERS
                ) as Int
            )
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.play_container, ModeSelectionFragment(), "MODE")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()

        /** Permission handling **/
        val permissionCheck =
            ContextCompat.checkSelfPermission(
                applicationContext!!,
                Manifest.permission.RECORD_AUDIO
            )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSIONS_REQUEST_RECORD_AUDIO
            )
    }

    /**
     * Removes the provided fragment from the fragment manager list
     */
    private fun removeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .remove(fragment)
            .commit()
    }

    /**
     * Removes all fragments from the fragment manager
     */
    fun removeAllFragments() {
        for (fragment in supportFragmentManager.fragments) {
            removeFragment(fragment)
        }
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

    /**
     * Stop the main menu music
     *
     */
    override fun onStop() {
        MainMusic.pause()
        super.onStop()
    }

    /**
     * Resume the main menu music
     *
     */
    override fun onResume() {
        MainMusic.play()
        super.onResume()
    }
}