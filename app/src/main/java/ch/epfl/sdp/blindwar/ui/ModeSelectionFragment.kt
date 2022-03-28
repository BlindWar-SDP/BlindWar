package ch.epfl.sdp.blindwar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.domain.game.GameMode

class ModeSelectionFragment: Fragment() {

    private val model: GameInstanceViewModel by activityViewModels()
    private lateinit var regularButton: Button
    private lateinit var survivalButton: Button
    private lateinit var raceButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_mode_selection, container, false)
        regularButton = view.findViewById<Button>(R.id.regularButton).also{selectMode(it)}
        raceButton = view.findViewById<Button>(R.id.raceButton).also{selectMode(it)}
        survivalButton = view.findViewById<Button>(R.id.survivalButton).also{selectMode(it)}
        return view
    }

    private fun selectMode(view: View) {
        view.setOnClickListener{
            model.setGameMode(when(view.id) {
                R.id.raceButton -> GameMode.TIMED
                R.id.survivalButton -> GameMode.SURVIVAL
                else -> GameMode.REGULAR
            })
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.mode_container, PlaylistSelectionFragment())
            ?.addToBackStack(this.tag)
            ?.commit()
        }
    }
}