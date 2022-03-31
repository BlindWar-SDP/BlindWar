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

class PlaylistSelectionFragment: Fragment() {
    private val gameInstanceViewModel: GameInstanceViewModel by activityViewModels()
    private lateinit var startButton: Button
    private lateinit var backButton: ImageButton

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
                    ?.replace(R.id.play_container, AnimatedDemoFragment(), "DEMO")
                    ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    ?.commit()
            }
        }

        backButton = view.findViewById<ImageButton>(R.id.back_button_playlist).also {
            it.setOnClickListener{
                activity?.onBackPressed()
            }
        }

        return view
    }
}