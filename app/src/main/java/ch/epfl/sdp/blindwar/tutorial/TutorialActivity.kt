package ch.epfl.sdp.blindwar.tutorial

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import ch.epfl.sdp.blindwar.R

class TutorialActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var viewPager: ViewPager2 = findViewById(R.id.pager)

        val fragments = arrayListOf<Fragment>(TutorialFragment(), TutorialFragment())

        val adapter = ViewPagerAdapter(fragments, this)
        viewPager.adapter = adapter
    }
}