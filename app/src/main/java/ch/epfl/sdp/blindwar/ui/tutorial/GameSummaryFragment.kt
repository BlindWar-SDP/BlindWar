package ch.epfl.sdp.blindwar.ui.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.ui.solo.AnimatedDemoFragment
import ch.epfl.sdp.blindwar.ui.solo.PlayActivity

class GameSummaryFragment : Fragment() {

    private var fragments: MutableList<Fragment> = mutableListOf()
    private lateinit var quit: ImageButton
    private lateinit var replay: ImageButton
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View =
            inflater.inflate(R.layout.fragment_game_summary, container, false)

        viewPager = view.findViewById(R.id.pager)
        adapter = ViewPagerAdapter(fragments, requireActivity())
        viewPager.adapter = adapter

        quit = view.findViewById<ImageButton>(R.id.quit).also { button ->
            button.setOnClickListener{
                activity?.onBackPressed()
            }
        }

        replay = view.findViewById<ImageButton>(R.id.replay).also{ button ->
            button.setOnClickListener{
                (activity!! as PlayActivity).removeAllFragments()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.play_container, AnimatedDemoFragment(), "DEMO")
                    ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    ?.commit()
            }
        }

        return view
    }

    fun setSongFragment(songFragment: SongSummaryFragment) {
        fragments.add(songFragment)
    }
}