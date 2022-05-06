package ch.epfl.sdp.blindwar.game.solo

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
import ch.epfl.sdp.blindwar.audio.MusicViewModel
import ch.epfl.sdp.blindwar.game.model.config.GameFormat
import ch.epfl.sdp.blindwar.game.solo.fragments.GameSummaryFragment
import ch.epfl.sdp.blindwar.game.solo.fragments.ModeSelectionFragment
import ch.epfl.sdp.blindwar.game.solo.fragments.SongSummaryFragment
import ch.epfl.sdp.blindwar.game.util.GameActivity
import ch.epfl.sdp.blindwar.game.viewmodels.GameInstanceViewModel
import ch.epfl.sdp.blindwar.profile.viewmodel.ProfileViewModel

/**
 * Contains the UI logic to play a solo game
 *
 * @constructor creates a PlayActivity
 */
class SoloActivity : GameActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        // Set the game format
        gameInstanceViewModel.setGameFormat(GameFormat.SOLO)

        supportFragmentManager.beginTransaction()
            .replace(R.id.play_container, ModeSelectionFragment(), "MODE")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()

        /** Permission handling **/
        val permissionCheck =
            ContextCompat.checkSelfPermission(applicationContext!!, Manifest.permission.RECORD_AUDIO)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSIONS_REQUEST_RECORD_AUDIO
            )
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