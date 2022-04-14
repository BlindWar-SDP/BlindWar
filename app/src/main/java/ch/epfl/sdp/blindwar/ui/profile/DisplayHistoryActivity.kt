package ch.epfl.sdp.blindwar.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindwar.R

class DisplayHistoryActivity : AppCompatActivity() {

    private var titles = mutableListOf<String>()
    private var artists = mutableListOf<String>()
    private var images = mutableListOf<Int>()
    private val musicRecyclerView: RecyclerView = R.id.musicRecyclerView as RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_history)

        postToList()

        musicRecyclerView.layoutManager = LinearLayoutManager(this)
        musicRecyclerView.adapter = MusicDisplayRecyclerAdapter(titles, artists, images)


    }

    private fun addToList(title: String, artist: String, image: Int) {
        titles.add(title)
        artists.add(artist)
        images.add(image)
    }

    private fun postToList() {
        for (i in 1..3) {
            addToList("HELLO", "JOJO", R.mipmap.ic_launcher_round_base)
        }
    }
}