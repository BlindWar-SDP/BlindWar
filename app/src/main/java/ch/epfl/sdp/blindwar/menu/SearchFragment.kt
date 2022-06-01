package ch.epfl.sdp.blindwar.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.data.music.DisplayableViewModel
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadataRepository
import ch.epfl.sdp.blindwar.game.model.Displayable
import ch.epfl.sdp.blindwar.game.util.DisplayableItemAdapter
import ch.epfl.sdp.blindwar.game.viewmodels.GameInstanceViewModel
import ch.epfl.sdp.blindwar.profile.viewmodel.ProfileViewModel

class SearchFragment : Fragment() {
    private lateinit var musicMetadataRecyclerView: RecyclerView
    private lateinit var musicMetadataRepository: MusicMetadataRepository
    private lateinit var gameInstanceViewModel: GameInstanceViewModel
    private lateinit var adapter: DisplayableItemAdapter
    private lateinit var searchBar: SearchView
    private val displayableViewModel: DisplayableViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_playlist_selection, container, false)
        musicMetadataRecyclerView = view.findViewById(R.id.playlistRecyclerView)
        musicMetadataRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

        view.findViewById<TextView>(R.id.textView).text = getString(R.string.Search)

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
        musicMetadataRecyclerView.adapter = DisplayableItemAdapter(
            list,
            requireContext(),
            view,
            gameInstanceViewModel,
            profileViewModel,
            parentFragmentManager
        )
        adapter = musicMetadataRecyclerView.adapter as DisplayableItemAdapter
    }

    /**
     * Sets up the search view
     */
    private fun setUpSearchView() {
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                displayableViewModel.queryMetadata(query!!)
                onQueryTextChange(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.getFilter().filter(newText)
                return true
            }
        })
    }
}