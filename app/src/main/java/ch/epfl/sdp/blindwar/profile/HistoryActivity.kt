package ch.epfl.sdp.blindwar.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.profile.fragments.DisplayHistoryFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HistoryActivity : AppCompatActivity() {
    //private val database = UserDatabase

    /**
     * Generates the layout and sets up bottom navigation
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_menu)

        val likedMusicType = "liked musics"
        val matchHistoryType = "match history"

        showFragment((DisplayHistoryFragment.newInstance(likedMusicType)))

        findViewById<BottomNavigationView>(R.id.historyBottomNavigationView).setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_liked_musics -> showFragment(
                    DisplayHistoryFragment.newInstance(
                        likedMusicType
                    )
                )
                R.id.item_match_history -> showFragment(
                    DisplayHistoryFragment.newInstance(
                        matchHistoryType
                    )
                )
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
            replace(R.id.fragment_history_menu_container, fragment)
            commit()
        }
    }
}