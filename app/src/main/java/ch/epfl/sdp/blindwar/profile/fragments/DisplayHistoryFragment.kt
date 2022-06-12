package ch.epfl.sdp.blindwar.profile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.game.model.GameResult
import ch.epfl.sdp.blindwar.profile.model.Result
import ch.epfl.sdp.blindwar.profile.model.User
import ch.epfl.sdp.blindwar.profile.util.LeaderboardRecyclerAdapter
import ch.epfl.sdp.blindwar.profile.util.MatchHistoryRecyclerAdapter
import ch.epfl.sdp.blindwar.profile.util.MusicDisplayRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth

class DisplayHistoryFragment : Fragment() {

    private lateinit var musicRecyclerView: RecyclerView
    private var titles = mutableListOf<String>()
    private var artists = mutableListOf<String>()
    private var images = mutableListOf<String>()
    private var winsList = mutableListOf<String>()
    private var lossesList = mutableListOf<String>()
    private var victoriesList = mutableListOf<String>()
    private var gameTimesList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_display_music, container, false)

        postToList()
        musicRecyclerView = view.findViewById(R.id.musicRecyclerView)
        musicRecyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)

        return view
    }

    /**
     * Add element of MusicMetadata to appropriate list.
     * @param title
     * @param artist
     * @param image
     */
    private fun addToList(title: String, artist: String, image: String) {
        titles.add(title)
        artists.add(artist)
        images.add(image)
    }

    /**
     * Fetch likedMusics from user and call addToList(with the listener) to add the element
     * to the lists that will be displayed.
     * Creates a different display based on the argument of the Fragment.
     */
    private fun postToList() {
        val historyType = arguments?.getString(HISTORY_TYPE)
        val currentUser = FirebaseAuth.getInstance().currentUser

        currentUser?.let {
            when (historyType) {
                LIKED_MUSIC_TYPE -> {
                    setLikedMusic(it.uid)
                }
                MATCH_HISTORY_TYPE -> {
                    setMatchHistory(it.uid)
                }
                LEADERBOARD_TYPE -> {
                    setLeaderboard(it.uid)
                }
            }
        }
    }

    private fun setLikedMusic(uid: String) {
        UserDatabase.userDoc(uid).get().addOnSuccessListener { snapshot ->
            snapshot.toObject(User::class.java)?.let { user ->
                val likedMusics: MutableList<MusicMetadata> = user.likedMusics
                for (music in likedMusics) {
                    addToList(music.name, music.author, music.cover)
                }
            }
            musicRecyclerView.adapter = MusicDisplayRecyclerAdapter(titles, artists, images)
        }
    }


    private fun setMatchHistory(uid: String) {
        UserDatabase.userDoc(uid).get().addOnSuccessListener { snapshot ->
            snapshot.toObject(User::class.java)?.let { user ->
                val matchHistory: MutableList<GameResult> = user.matchHistory
                for (match in matchHistory) {
                    // Create the String that will we displayed depending on the value
                    var result = "DRAW"
                    if (match.result == Result.WIN) {
                        result = "WIN"
                    } else if (match.result == Result.LOSS) {
                        result = "LOSS"
                    }
                    addToList(
                        "Rounds: " + match.gameNbrRound.toString(),
                        "Score: " + match.gameScore.toString(),
                        match.mode.toString() + "  " + match.gameMode.toString()
                    )
                    victoriesList.add(result)
                    gameTimesList.add(match.gameTime)
                }
            }
            musicRecyclerView.adapter = MatchHistoryRecyclerAdapter(
                victoriesList, gameTimesList,
                images, titles, artists
            )
        }
    }

    private fun setLeaderboard(uid: String) {
        UserDatabase.userRef.get().addOnSuccessListener { snapshot ->
            for (doc in snapshot) {
                doc.toObject(User::class.java).let { user ->
                    // The [1] at the end is to have the wins for multi mode only.
                    val wins = user.userStatistics.wins[1]
                    val losses = user.userStatistics.losses[1]
                    addToList("1", user.pseudo, user.userStatistics.elo.toString())
                    winsList.add(wins.toString())
                    lossesList.add(losses.toString())
                }
            }
        }
        // Create data class to contain data of an user to be displayed on the leaderboard
        data class LeaderboardUserData(
            val elo: String,
            val wins: String,
            val losses: String
        )

        // Create a List of (pseudo, elo) Pairs and order them by elo to have
        // an ordered leaderboard
        val pseudoUserData = mutableListOf<Pair<String, LeaderboardUserData>>()
        for (i in (0 until artists.size)) {
            val userData = LeaderboardUserData(images[i], winsList[i], lossesList[i])
            pseudoUserData.add(Pair(artists[i], userData))
        }
        val sortedPseudoUsers = pseudoUserData
            .sortedWith(compareBy({ it.second.elo }, { it.first })).asReversed()
        for (i in (0 until artists.size)) {
            artists[i] = sortedPseudoUsers[i].first
            images[i] = sortedPseudoUsers[i].second.elo
            winsList[i] = sortedPseudoUsers[i].second.wins
            lossesList[i] = sortedPseudoUsers[i].second.losses
        }

        musicRecyclerView.adapter = LeaderboardRecyclerAdapter(
            titles, artists, images,
            winsList, lossesList
        )
    }

    companion object {
        const val HISTORY_TYPE = "type"
        const val LIKED_MUSIC_TYPE = "liked musics"
        const val MATCH_HISTORY_TYPE = "match history"
        const val LEADERBOARD_TYPE = "leaderboard"

        fun newInstance(name: String): DisplayHistoryFragment {
            val fragment = DisplayHistoryFragment()
            val bundle = Bundle().apply {
                putString(HISTORY_TYPE, name)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}