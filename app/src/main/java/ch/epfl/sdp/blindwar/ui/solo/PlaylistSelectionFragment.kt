package ch.epfl.sdp.blindwar.ui.solo

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
import ch.epfl.sdp.blindwar.domain.game.Tutorial.fifaPlaylist
import ch.epfl.sdp.blindwar.domain.game.Tutorial.testingPlaylist
import ch.epfl.sdp.blindwar.domain.game.Tutorial.tutorialPlaylist

open class PlaylistSelectionFragment: Fragment() {
    //private lateinit var backButton: ImageButton
    private val gameInstanceViewModel: GameInstanceViewModel by activityViewModels()
    private lateinit var startButton: Button
    protected lateinit var playlistRecyclerView: RecyclerView
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

        val playlistCopy = ArrayList<PlaylistModel>().apply {
            addAll(arrayListOf(fifaPlaylist, tutorialPlaylist, testingPlaylist))
        }

        playlistRecyclerView = view.findViewById(R.id.playlistRecyclerView)
        playlistRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

        //attach adapter to list
        playlistRecyclerView.adapter = PlaylistAdapter(playlistCopy, requireActivity(), view, gameInstanceViewModel)
        adapter = playlistRecyclerView.adapter as PlaylistAdapter

        setUpSearchView()

        /** TODO: Test in the next sprint
        backButton = view.findViewById<ImageButton>(R.id.back_button_playlist).also {
        it.setOnClickListener{
        activity?.onBackPressed()
        }
        } **/


        return view
    }

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