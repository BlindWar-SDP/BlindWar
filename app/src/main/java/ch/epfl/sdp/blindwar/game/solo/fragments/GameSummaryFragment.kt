package ch.epfl.sdp.blindwar.game.solo.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.GameFragment
import ch.epfl.sdp.blindwar.game.model.config.GameFormat
import ch.epfl.sdp.blindwar.game.util.GameSettingsActivity
import ch.epfl.sdp.blindwar.game.util.ViewPagerAdapter
import ch.epfl.sdp.blindwar.game.viewmodels.GameInstanceViewModel
import ch.epfl.sdp.blindwar.menu.MainMenuActivity

/**
 * Game over fragment displayed after a game is completed
 *
 * @constructor creates a GameSummaryFragment
 */
class GameSummaryFragment : Fragment() {

    private var fragments: MutableList<Fragment> = mutableListOf()
    private lateinit var quit: ImageButton
    private lateinit var replay: ImageButton
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2
    private val gameInstanceViewModel: GameInstanceViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_game_summary, container, false)

        viewPager = view.findViewById(R.id.pager)
        adapter = ViewPagerAdapter(fragments, requireActivity())
        viewPager.adapter = adapter

        quit = view.findViewById<ImageButton>(R.id.quit).also { button ->
            button.setOnClickListener {
                startActivity(Intent(requireContext(), MainMenuActivity::class.java))
            }
        }
        // If the game is in multiplayer, add the final score
        // Switch between the two different game view model
        if (gameInstanceViewModel.gameInstance.value?.gameFormat == GameFormat.MULTI) {
            fragments.add(ScoreFragment())
            //Remove replay in multiplayer
            view.findViewById<ImageButton>(R.id.replay).visibility = View.GONE
        } else {
            view.findViewById<ImageButton>(R.id.replay).visibility = View.VISIBLE
            replay = view.findViewById<ImageButton>(R.id.replay).also { button ->
                button.setOnClickListener {
                    (requireActivity() as GameSettingsActivity).removeAllFragments()
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.play_container, GameFragment(), "DEMO")
                        ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        ?.commit()
                }
            }
        }
        return view
    }

    /**
     * Add a song fragment to the pager
     *
     * @param songFragment to add in the list
     */
    fun setSongFragment(songFragment: SongSummaryFragment) {
        fragments.add(songFragment)
    }
}