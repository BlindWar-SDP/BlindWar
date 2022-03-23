package ch.epfl.sdp.blindwar.ui.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import ch.epfl.sdp.blindwar.R

class GameSummaryFragment : Fragment() {

    private var fragments: MutableList<Fragment> = mutableListOf()
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View =
            inflater!!.inflate(R.layout.fragment_game_summary, container, false)

        viewPager = view.findViewById(R.id.pager)
        adapter = ViewPagerAdapter(fragments, requireActivity())
        viewPager.adapter = adapter

        return view
    }

    fun setSongFragment(songFragment: SongSummaryFragment) {
        fragments.add(songFragment)
    }
}