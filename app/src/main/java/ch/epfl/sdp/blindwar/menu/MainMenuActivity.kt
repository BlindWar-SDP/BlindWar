package ch.epfl.sdp.blindwar.menu

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.data.music.DisplayableViewModel
import ch.epfl.sdp.blindwar.profile.fragments.ProfileFragment
import ch.epfl.sdp.blindwar.profile.viewmodel.ProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainMenuActivity : AppCompatActivity() {
    //private val database = UserDatabase
    private lateinit var bottomMenu: BottomNavigationView
    private val profileViewModel: ProfileViewModel by viewModels()
    private val displayableViewModel: DisplayableViewModel by viewModels()

    @SuppressLint("UseCompatLoadingForDrawables")
    /**
     * Generates the layout and sets up bottom navigation
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        // following lines hide the status bar
        // (the one with the time/setting/network/cellular/battery)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        // this hide the top bar with the App Label/ back-button / ...
//        supportActionBar?.hide()

        showFragment(PlayMenuFragment())

        bottomMenu = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_play -> showFragment(PlayMenuFragment())
                R.id.item_search -> showFragment(SearchFragment())
                R.id.item_profile -> showFragment(ProfileFragment())
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
        this.moveTaskToBack(true);
    }
}