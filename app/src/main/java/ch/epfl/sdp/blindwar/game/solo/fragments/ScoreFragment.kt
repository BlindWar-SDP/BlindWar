package ch.epfl.sdp.blindwar.game.solo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.MatchDatabase
import ch.epfl.sdp.blindwar.game.multi.model.Match
import ch.epfl.sdp.blindwar.game.util.ScoreboardAdapter
import ch.epfl.sdp.blindwar.game.viewmodels.GameInstanceViewModel
import ch.epfl.sdp.blindwar.profile.viewmodel.ProfileViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ScoreFragment : Fragment() {

    private lateinit var scoreboard: RecyclerView
    private lateinit var scoreboardAdapter: ScoreboardAdapter
    private val gameInstanceViewModel: GameInstanceViewModel by activityViewModels()
    private lateinit var matchId: String
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_score, container, false)

        val buttonLeave = view.findViewById<View>(R.id.leave_multi_score) as Button
        buttonLeave.setOnClickListener {
            onLeave()
        }

        // Get the scoreboard
        scoreboard = view.findViewById(R.id.result_scoreboard)

        // Get the data from the match
        val listPseudos: List<String> = gameInstanceViewModel.match?.listPlayers!!
        val listResult = gameInstanceViewModel.match?.listResult

        // Add a listener to update the score
        val databaseListener = EventListener<DocumentSnapshot> { value, _ ->
            val match = value?.toObject(Match::class.java)

            // Set the score board on new result
            gameInstanceViewModel.match?.listResult = match?.listResult
            scoreboardAdapter.updateScoreboardFromList(gameInstanceViewModel.match?.listResult)
            scoreboardAdapter.notifyDataSetChanged()
        }

        // Get the match id
        matchId = arguments?.get("matchId") as String
        MatchDatabase.addListener(matchId, Firebase.firestore, databaseListener)

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

    private fun onLeave() {
        MatchDatabase.getMatchSnapshot(matchId, Firebase.firestore)?.let {
            val match = it.toObject(Match::class.java)
            val gameInstanceShared = match?.game
            gameInstanceViewModel.gameInstance.value = gameInstanceShared
        }

        val matchRef = Firebase.firestore.collection("match").document(matchId)
        Firebase.firestore.runTransaction { transaction ->
            val snapshot = transaction.get(matchRef)
            val match = snapshot.toObject(Match::class.java)
            var pseudo = ""
            profileViewModel.user.observe(viewLifecycleOwner) { it ->
                it.pseudo.also { pseudo = it }
            }

            // Mark that the player has leave the game
            val listPseudo = match?.listPseudo!!
            val index = listPseudo.indexOf(pseudo)
            listPseudo[index] = ""
            transaction.update(matchRef, "listPseudo", listPseudo)

            // If every player has finished, destroy the match
            if (!listPseudo.any { it != "" }) {
                MatchDatabase.removeMatch(matchId, Firebase.firestore)
            }
            // Success
            null
        }
    }
}