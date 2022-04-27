package ch.epfl.sdp.blindwar.menu

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.profile.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainMenuActivity : AppCompatActivity() {
    //private val database = UserDatabase
    private lateinit var bottomMenu: BottomNavigationView

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        showFragment(PlayMenuFragment())

        bottomMenu = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomMenu.setOnItemSelectedListener {
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
}