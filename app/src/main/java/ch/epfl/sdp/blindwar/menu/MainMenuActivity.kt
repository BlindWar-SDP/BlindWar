package ch.epfl.sdp.blindwar.menu

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.util.MainMusic
import ch.epfl.sdp.blindwar.login.PermissionHandler
import ch.epfl.sdp.blindwar.profile.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainMenuActivity : AppCompatActivity() {
    //private val database = UserDatabase
    private lateinit var bottomMenu: BottomNavigationView

    @SuppressLint("UseCompatLoadingForDrawables")
    /**
     * Generates the layout and sets up bottom navigation
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        PermissionHandler.handle(applicationContext, this)
        // Start the main menu music
        MainMusic.prepareAndPlay(this)

        val play = PlayMenuFragment()
        val search = SearchFragment()
        val profile = ProfileFragment()

        showFragment(PlayMenuFragment())

        bottomMenu = findViewById(R.id.bottomNavigationView)

        bottomMenu.setOnItemSelectedListener {
            when (it.itemId) {
                // Avoid creating a new fragment on each navigation event
                R.id.item_play -> showFragment(play)
                R.id.item_search -> showFragment(search)
                R.id.item_profile -> showFragment(profile)
            }
            true
        }
    }

    /**
     * Shows the selected fragment
     *
     * @param fragment to show
     */
    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_menu_container, fragment)
            commit()
        }
    }

    /**
     * Minimizes the app if the back button is pressed
     *
     */
    override fun onBackPressed() {
        this.moveTaskToBack(true)
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