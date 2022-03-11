package ch.epfl.sdp.blindwar.tutorial

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import ch.epfl.sdp.blindwar.R

class ViewPagerAdapter(
    val items: List<Fragment>,
    activity: AppCompatActivity
):FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return items[position]
    }
}