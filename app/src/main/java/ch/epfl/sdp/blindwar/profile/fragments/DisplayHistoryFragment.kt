package ch.epfl.sdp.blindwar.profile.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.data.music.metadata.URIMusicMetadata
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.game.model.GameResult
import ch.epfl.sdp.blindwar.game.viewmodels.GameInstanceViewModel
import ch.epfl.sdp.blindwar.profile.model.Result
import ch.epfl.sdp.blindwar.profile.model.User
import ch.epfl.sdp.blindwar.profile.util.LeaderboardRecyclerAdapter
import ch.epfl.sdp.blindwar.profile.util.MatchHistoryRecyclerAdapter
import ch.epfl.sdp.blindwar.profile.util.MusicDisplayRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class DisplayHistoryFragment : Fragment() {

    private lateinit var musicRecyclerView: RecyclerView
    private var titles = mutableListOf<String>()
    private var artists = mutableListOf<String>()
    private var images = mutableListOf<String>()
    private var winsList = mutableListOf<String>()
    private var lossesList = mutableListOf<String>()
    private var victoriesList = mutableListOf<String>()
    private var gameTimesList = mutableListOf<String>()

    private val LIKED_MUSIC_TYPE = "liked musics"
    private val MATCH_HISTORY_TYPE = "match history"
    private val LEADERBOARD_TYPE = "leaderboard"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_display_music, container, false)

        postToList()
        musicRecyclerView = view.findViewById(R.id.musicRecyclerView)
        musicRecyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)


        // showing the back button in action bar
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

        if (currentUser != null) {
            if (historyType == LIKED_MUSIC_TYPE) {
                UserDatabase.addUserListener(currentUser.uid, userLikedMusicsListener)
            }

            if (historyType == MATCH_HISTORY_TYPE) {
                UserDatabase.addUserListener(currentUser.uid, userMatchHistoryListener)
            }

            if (historyType == LEADERBOARD_TYPE) {
                UserDatabase.addSingleEventAllUsersListener(leaderboardListener)
            }
        }

        /*
        else {
            for (i in 1..20) {
                addToList("HELLO", "JOJO", R.mipmap.ic_launcher_round_base)
            }
        }
         */
    }

    private val userLikedMusicsListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val user: User? = try {
                dataSnapshot.getValue<User>()
            } catch (e: DatabaseException) {
                null
            }
            if (user != null) {
                val likedMusics: MutableList<URIMusicMetadata> = user.likedMusics
                for (music in likedMusics) {
                    addToList(music.title, music.artist, music.imageUrl)
                }
            } else {
                for (i in 1..10) {
                    addToList("HELLO", "JOJO", "no image")
                }
            }
            musicRecyclerView.adapter = MusicDisplayRecyclerAdapter(titles, artists, images)
        }


        override fun onCancelled(databaseError: DatabaseError) {
            // Log.w("CANCELED REQUEST", "userLikedMusic:onCancelled", databaseError.toException())
        }
    }

    private val userMatchHistoryListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val user: User? = try {
                dataSnapshot.getValue<User>()
            } catch (e: DatabaseException) {
                null
            }
            if (user != null) {
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
            } else {
                for (i in 1..10) {
                    addToList("HELLO", "JOJO", "no image")
                }
            }
            musicRecyclerView.adapter = MatchHistoryRecyclerAdapter(
                victoriesList, gameTimesList,
                images, titles, artists
            )
        }


        override fun onCancelled(databaseError: DatabaseError) {
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }

    /**
     * Retrieves all Users from the database an get their pseudo and elo
     */
    private val leaderboardListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val usersMap = try {
                dataSnapshot.getValue<Map<String, Any>>()
            } catch (e: DatabaseException) {
                null
            }
            if (usersMap != null) {
                for (user in usersMap.values) {
                    val userMap: Map<String, String> = user as Map<String, String>
                    val userStatMap: HashMap<String, Map<String, Long>> =
                        user as HashMap<String, Map<String, Long>>
                    val userWinsMap: HashMap<String, Map<String, ArrayList<Long>>> =
                        user as HashMap<String, Map<String, ArrayList<Long>>>
                    val pseudo = userMap["pseudo"]
                    val elo = userStatMap["userStatistics"]?.get("elo")

                    // The get(1) at the end is to have the wins for multi mode only.
                    val wins = userWinsMap["userStatistics"]?.get("wins")?.get(1)
                    val losses = userWinsMap["userStatistics"]?.get("losses")?.get(1)
                    if (pseudo != null && elo != null && pseudo != "" &&
                        wins != null && losses != null
                    ) {
                        addToList("1", pseudo, elo.toString())
                        winsList.add(wins.toString())
                        lossesList.add(losses.toString())
                    }
                }
            } else {
                for (i in 1..10) {
                    addToList("HELLO", "JOJO", "no image")
                }
            }
            // Create data class to contain data of an user to be displayed on the leaderboard
            data class LeaderboardUserData(val elo: String, val wins: String, val losses: String)

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


        override fun onCancelled(databaseError: DatabaseError) {
            // Log.w("CANCELED REQUEST", "userMatchHistory:onCancelled", databaseError.toException())
        }
    }

    companion object {
        const val HISTORY_TYPE = "type"


        fun newInstance(name: String): DisplayHistoryFragment {
            val fragment = DisplayHistoryFragment()

            val bundle = Bundle().apply {
                putString(HISTORY_TYPE, name)
            }

            fragment.arguments = bundle

            return fragment
        }
    }


    /*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    } */
}