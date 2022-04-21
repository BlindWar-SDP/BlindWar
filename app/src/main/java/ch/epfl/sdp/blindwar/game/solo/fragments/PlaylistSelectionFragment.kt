package ch.epfl.sdp.blindwar.game.solo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.util.Tutorial.fifaPlaylist
import ch.epfl.sdp.blindwar.game.util.Tutorial.testingPlaylist
import ch.epfl.sdp.blindwar.game.util.Tutorial.tutorialPlaylist
import ch.epfl.sdp.blindwar.game.util.PlaylistAdapter
import ch.epfl.sdp.blindwar.game.model.Playlist
import ch.epfl.sdp.blindwar.game.viewmodels.GameInstanceViewModel

/**
 * Fragment that let the user choose the game playlist
 *
 * @constructor creates a PlaylistSelectionFragment
 */
class PlaylistSelectionFragment: Fragment() {
    //private lateinit var backButton: ImageButton
    private val gameInstanceViewModel: GameInstanceViewModel by activityViewModels()
    private lateinit var startButton: Button
    private lateinit var playlistRecyclerView: RecyclerView
    private lateinit var searchBar: SearchView
    private lateinit var adapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_playlist_selection, container, false)

        /** Lyrics parser
        val lyricsString = activity?.assets?.open("syncedLyrics/wegue_lrc.json")?.bufferedReader().use { it?.readText() }
        val lyrics = Gson().fromJson(lyricsString, SyncedLyrics::class.java)
        Log.d("LYRICS", lyrics.lyrics.toString())
        **/

        searchBar = view.findViewById(R.id.searchBar)


        val playlistCopy = ArrayList<Playlist>().apply {
            addAll(arrayListOf(fifaPlaylist, tutorialPlaylist, testingPlaylist))
        }

        playlistRecyclerView = view.findViewById(R.id.playlistRecyclerView)
        playlistRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

        //attach adapter to list
        playlistRecyclerView.adapter = PlaylistAdapter(playlistCopy, requireActivity(), view, gameInstanceViewModel)
        adapter = playlistRecyclerView.adapter as PlaylistAdapter

        setUpSearchView()

        /** TODO: Add button task
        backButton = view.findViewById<ImageButton>(R.id.back_button_playlist).also {
        it.setOnClickListener{
        activity?.onBackPressed()
        }
        } **/

        return view
    }

    /**
     * Sets up the search view
     */
    private fun setUpSearchView() {
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = onQueryTextChange(query)

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.getFilter().filter(newText);
                return true
            }
        })
    }
}