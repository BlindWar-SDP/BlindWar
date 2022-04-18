package ch.epfl.sdp.blindwar.ui.multi

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.domain.game.PlayerListAdapter
import java.util.*

class MultiPlayerActivity : AppCompatActivity() {

    var i = 1
    private lateinit var playerListAdapter: PlayerListAdapter
    var playersAndPoints = listOf(Pair(i, "Marty"), Pair(2, "Joris"), Pair(3, "Nael"), Pair(4, "Arthur"), Pair(5, "Paul"), Pair(6, "Henrique"))

    /**
     * Creates basic layout according to xml
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiplayer)

        // showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get the recycler view and set its custom adapter
        this.playerListAdapter = PlayerListAdapter(playersAndPoints)
        val playlistRecyclerView: RecyclerView = findViewById(R.id.player_list_recyclerview)
        playlistRecyclerView.adapter = playerListAdapter

        (findViewById<Button>(R.id.test_list)).setOnClickListener {
            ++i
            playersAndPoints = listOf(Pair(i, "Marty"), Pair(2, "Joris"), Pair(3, "Nael"), Pair(4, "Arthur"), Pair(5, "Paul"), Pair(6, "Henrique"))
            playerListAdapter.dataSet  = playersAndPoints
            playerListAdapter.notifyDataSetChanged()
        }
    }
}
