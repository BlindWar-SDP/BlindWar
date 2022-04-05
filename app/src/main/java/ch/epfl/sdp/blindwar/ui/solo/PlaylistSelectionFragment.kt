package ch.epfl.sdp.blindwar.ui.solo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.widget.SearchView
import androidx.compose.ui.Alignment
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.data.SyncedLyrics
import ch.epfl.sdp.blindwar.domain.game.SongMetaData
import ch.epfl.sdp.blindwar.ui.solo.animated.AnimatedDemoFragment
import com.google.gson.Gson
import java.util.Collections.addAll

class PlaylistSelectionFragment: Fragment() {
    /** Keep : Useful for the next sprint **/
    //private val gameInstanceViewModel: GameInstanceViewModel by activityViewModels()
    //private lateinit var backButton: ImageButton
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

        startButton = view.findViewById<Button>(R.id.startDemo).also{
            it.setOnClickListener{
                // Set playlist in the GameInstance view model
                // Start a Game depending on the Game Mode

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace((view?.parent as ViewGroup).id, DemoFragment(), "DEMO")
                    ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    ?.commit()
            }
        }

        /** Lyrics parser**/
        val lyricsString = activity?.assets?.open("syncedLyrics/wegue_lrc.json")?.bufferedReader().use { it?.readText() }
        val lyrics = Gson().fromJson(lyricsString, SyncedLyrics::class.java)
        Log.d("LYRICS", lyrics.lyrics.toString())



        searchBar = view.findViewById(R.id.searchBar)

        //create a copy of city list
        val url =
            "https://p.scdn.co/mp3-preview/6cc1de8747a673edf568d78a37b03eab86a65c21?cid=774b29d4f13844c495f206cafdad9c86"

        val url2 =
            ""

        val fifaOst = Playlist("FIFA 13 OST", arrayListOf(SongMetaData("name", "author", url)), "https://i.scdn.co/image/ab67706c0000bebba1371bd946a7bc3f61f83db4")
        val playlistCopy = ArrayList<Playlist>().apply {
            addAll(arrayListOf(fifaOst, fifaOst, fifaOst, fifaOst, fifaOst, fifaOst))
        }

        playlistRecyclerView = view.findViewById(R.id.playlistRecyclerView)
        playlistRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

        //attach adapter to list
        playlistRecyclerView.adapter = PlaylistAdapter(playlistCopy, requireActivity(), view)

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
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.getFilter().filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.getFilter().filter(newText);
                return true
            }
        })
    }
}