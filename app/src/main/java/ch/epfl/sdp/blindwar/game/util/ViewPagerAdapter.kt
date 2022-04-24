package ch.epfl.sdp.blindwar.game.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Adapter that is passed to the ViewPager object to support fragments
 *
 * @param items
 * @param activity
 */
class ViewPagerAdapter(
    private val items: List<Fragment>,
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return items[position]
    }
}