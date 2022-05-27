package ch.epfl.sdp.blindwar.game.solo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.util.ScoreboardAdapter
import ch.epfl.sdp.blindwar.game.viewmodels.GameInstanceViewModel

class ScoreFragment(): Fragment() {

    private lateinit var scoreboard: RecyclerView
    private lateinit var scoreboardAdapter: ScoreboardAdapter
    private val gameInstanceViewModel: GameInstanceViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_score, container, false)

        // Get the scoreboard
        scoreboard = view.findViewById(R.id.result_scoreboard)

        // Get the data from the match
        val listPseudos: List<String> = gameInstanceViewModel.match?.listPlayers!!
        val listResult = gameInstanceViewModel.match?.listResult

        // Create the adapter for the score board
        scoreboardAdapter = ScoreboardAdapter(listPseudos)

        // Set the data for the  scoreboard
        scoreboardAdapter.updateScoreboardFromList(listResult)

        val layoutManager = LinearLayoutManager(context)
        scoreboard.layoutManager = layoutManager
        scoreboard.adapter = scoreboardAdapter
        scoreboardAdapter.notifyDataSetChanged()

        return view
    }
}