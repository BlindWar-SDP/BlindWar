package ch.epfl.sdp.blindwar.game.util

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import ch.epfl.sdp.blindwar.game.viewmodels.GameInstanceViewModel
import ch.epfl.sdp.blindwar.profile.viewmodel.ProfileViewModel

abstract class GameActivity: AppCompatActivity() {
    val gameInstanceViewModel: GameInstanceViewModel  by viewModels()
    protected val profileViewModel: ProfileViewModel by viewModels()

    /**
     * Removes the provided fragment from the fragment manager list
     */
    protected fun removeFragment(fragment: Fragment) {
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
}