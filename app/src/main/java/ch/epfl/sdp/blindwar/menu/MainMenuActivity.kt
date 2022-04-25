package ch.epfl.sdp.blindwar.menu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.profile.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainMenuActivity : AppCompatActivity() {
    //private val database = UserDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        showFragment(PlayMenuFragment())

        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnItemSelectedListener {
            when(it.itemId){
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

    override fun onBackPressed() {
        this.moveTaskToBack(true);
    }
}