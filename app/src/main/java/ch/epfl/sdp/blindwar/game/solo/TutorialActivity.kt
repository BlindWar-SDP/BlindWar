package ch.epfl.sdp.blindwar.game.solo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.util.ViewPagerAdapter
import ch.epfl.sdp.blindwar.game.solo.fragments.TutorialFragment

// TODO: Remove unused activity
class TutorialActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        val viewPager: ViewPager2 = findViewById(R.id.pager)
        val fragments =
            arrayListOf(TutorialFragment(), TutorialFragment())
        val adapter = ViewPagerAdapter(fragments, this)

        viewPager.adapter = adapter
    }
}