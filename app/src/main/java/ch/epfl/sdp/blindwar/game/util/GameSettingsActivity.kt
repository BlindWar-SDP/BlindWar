package ch.epfl.sdp.blindwar.game.util

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.model.config.GameFormat
import ch.epfl.sdp.blindwar.game.multi.MultiPlayerMenuActivity
import ch.epfl.sdp.blindwar.game.GameActivity
import ch.epfl.sdp.blindwar.game.solo.fragments.GameSummaryFragment
import ch.epfl.sdp.blindwar.game.solo.fragments.ModeSelectionFragment
import ch.epfl.sdp.blindwar.game.solo.fragments.SongSummaryFragment
import ch.epfl.sdp.blindwar.game.viewmodels.GameSettingsViewModel
import ch.epfl.sdp.blindwar.profile.viewmodel.ProfileViewModel

class GameSettingsActivity : AppCompatActivity() {
    val gameSettingsViewModel: GameSettingsViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

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

        val matchId = intent.extras?.get(MultiPlayerMenuActivity.MATCH_ID)
        if (matchId != null) {
            launchGameMulti(matchId as String)
        } else {

            // Set the game format
            val format: GameFormat = intent.extras!!.get(GAME_FORMAT_EXTRA_NAME) as GameFormat
            gameSettingsViewModel.setGameFormat(format)
            if (format == GameFormat.MULTI) {
                // TODO: Remove everywhere "isPrivate"
                gameSettingsViewModel.setMultiParameters(
                    false, intent.extras!!.get(
                        GAME_MAX_PLAYERS
                    ) as Int
                )
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.play_container, ModeSelectionFragment(), "MODE")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
        }
    }

    /**
     * Launch the Game in multiplayer mode
     *
     * @param matchId
     */
    private fun launchGameMulti(matchId: String) {

        val matchId = intent.extras?.get(MultiPlayerMenuActivity.MATCH_ID) as String?

        // Create the bundle with the match id
        val bundle = Bundle().apply {
            putString("match_id", matchId)
        }

        // Create the intent and give it the bundle
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtras(bundle)
        startActivity(Intent(this, GameActivity::class.java))
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
}