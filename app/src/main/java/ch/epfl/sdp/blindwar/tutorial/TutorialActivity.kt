package ch.epfl.sdp.blindwar.tutorial

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.GameTutorial

class TutorialActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        val viewPager: ViewPager2 = findViewById(R.id.pager)
        val fragments = arrayListOf<Fragment>(TutorialFragment(), TutorialFragment())
        val adapter = ViewPagerAdapter(fragments, this)
        assert(fragments.size == adapter.itemCount)

        viewPager.adapter = adapter
    }
}