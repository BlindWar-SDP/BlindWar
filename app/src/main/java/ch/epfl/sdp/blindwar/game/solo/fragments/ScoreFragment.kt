package ch.epfl.sdp.blindwar.game.solo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.util.ScoreboardAdapter

class ScoreFragment(): Fragment() {

    private lateinit var scoreboard: RecyclerView
    private lateinit var scoreboardAdapter: ScoreboardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_score, container, false)

        // Get the scoreboard
        scoreboard = view.findViewById(R.id.result_scoreboard)

        // Create the adapter for the score board
        scoreboardAdapter = ScoreboardAdapter(listOf("Marty", "Joris", "Nael", "Arthur", "Paul", "Henrique"))

        val layoutManager = LinearLayoutManager(context)
        scoreboard.layoutManager = layoutManager
        scoreboard.adapter = scoreboardAdapter
        scoreboardAdapter.notifyDataSetChanged()

        return view
    }
}