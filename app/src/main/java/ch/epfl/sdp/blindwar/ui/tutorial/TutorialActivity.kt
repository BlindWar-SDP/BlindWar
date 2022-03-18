package ch.epfl.sdp.blindwar.ui.tutorial

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import ch.epfl.sdp.blindwar.R

class TutorialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        val viewPager: ViewPager2 = findViewById(R.id.pager)
        val fragments =
            arrayListOf(TutorialFragment(), TutorialFragment(), TutorialContinueToDemoFragment())
        val adapter = ViewPagerAdapter(fragments, this)

        viewPager.adapter = adapter
    }
}