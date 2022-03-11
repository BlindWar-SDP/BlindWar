package ch.epfl.sdp.blindwar.tutorial

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.GameTutorial


class DemonstrationTutorialFragment : Fragment() {
    private lateinit var gameTutorial: GameTutorial
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View =
            inflater!!.inflate(R.layout.fragment_demonstration_tutorial, container, false)
        val btn: Button = view.findViewById(R.id.button)

        gameTutorial = activity?.applicationContext?.let { GameTutorial(it.assets) }!!

        btn.setOnClickListener { view ->
            gameTutorial.nextRound()
        }

        return view
    }
}