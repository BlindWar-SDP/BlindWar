package ch.epfl.sdp.blindwar.ui.solo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.ui.solo.animated.AnimatedDemoFragment

class PlaylistSelectionFragment: Fragment() {
    /** Keep : Useful for the next sprint **/
    //private val gameInstanceViewModel: GameInstanceViewModel by activityViewModels()
    //private lateinit var backButton: ImageButton
    private lateinit var startButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_playlist_selection, container, false)

        startButton = view.findViewById<Button>(R.id.startButton).also{
            it.setOnClickListener{
                // Set playlist in the GameInstance view model
                // Start a Game depending on the Game Mode

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace((view?.parent as ViewGroup).id, DemoFragment(), "DEMO")
                    ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    ?.commit()
            }
        }

        /** TODO: Test in the next sprint
        backButton = view.findViewById<ImageButton>(R.id.back_button_playlist).also {
            it.setOnClickListener{
                activity?.onBackPressed()
            }
        } **/

        return view
    }
}