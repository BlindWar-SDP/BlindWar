package ch.epfl.sdp.blindwar.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.data.music.DisplayableViewModel
import ch.epfl.sdp.blindwar.data.music.MusicMetadataRepository
import ch.epfl.sdp.blindwar.data.music.MusicMetadataViewModel
import ch.epfl.sdp.blindwar.data.music.MusicRepository
import ch.epfl.sdp.blindwar.game.model.Displayable
import ch.epfl.sdp.blindwar.game.util.DisplayableItemAdapter
import ch.epfl.sdp.blindwar.game.util.Tutorial
import ch.epfl.sdp.blindwar.game.viewmodels.GameInstanceViewModel
import ch.epfl.sdp.blindwar.game.viewmodels.PlaylistViewModel

class SearchFragment : Fragment() {

    private lateinit var musicMetadataRecyclerView: RecyclerView
    private lateinit var musicMetadataRepository: MusicMetadataRepository
    private lateinit var gameInstanceViewModel: GameInstanceViewModel
    private lateinit var adapter: DisplayableItemAdapter
    private lateinit var searchBar: SearchView
    private val displayableViewModel: DisplayableViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_playlist_selection, container, false)
        musicMetadataRecyclerView = view.findViewById(R.id.playlistRecyclerView)
        musicMetadataRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

        view.findViewById<TextView>(R.id.textView).text = "Search"

        gameInstanceViewModel = GameInstanceViewModel()
        musicMetadataRepository = MusicMetadataRepository()
        searchBar = view.findViewById(R.id.searchBar)
        setUpSearchView()

        displayableViewModel.metadata.observe(requireActivity()) {
            if (!it.isNullOrEmpty()) {
                resetRecyclerView(view, it)
            }
        }

        return view
    }

    /**
     * Resets the playlist recycler view with the updated list of playlist
     */
    private fun resetRecyclerView(view: View, list: ArrayList<Displayable>) {
        musicMetadataRecyclerView.adapter = DisplayableItemAdapter(list, requireActivity(), view, gameInstanceViewModel)
        adapter = musicMetadataRecyclerView.adapter as DisplayableItemAdapter
    }

    /**
     * Sets up the search view
     */
    private fun setUpSearchView() {
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //displayableViewModel.fetchMusicMetadata(query!!)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }
}