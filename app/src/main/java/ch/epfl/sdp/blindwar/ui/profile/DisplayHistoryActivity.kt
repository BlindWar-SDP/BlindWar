package ch.epfl.sdp.blindwar.ui.profile

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.data.music.MusicMetadata
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.user.AppStatistics
import ch.epfl.sdp.blindwar.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class DisplayHistoryActivity : AppCompatActivity() {

    private var titles = mutableListOf<String>()
    private var artists = mutableListOf<String>()
    private var images = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_music)

        postToList()
        var musicRecyclerView: RecyclerView = findViewById(R.id.musicRecyclerView)
        musicRecyclerView.layoutManager = LinearLayoutManager(this)
        musicRecyclerView.adapter = MusicDisplayRecyclerAdapter(titles, artists, images)

        // showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    private fun addToList(title: String, artist: String, image: Int) {
        titles.add(title)
        artists.add(artist)
        images.add(image)
    }

    private fun postToList() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            UserDatabase.addUserListener(currentUser.uid, userLikedMusicsListener)
        } else {
            for (i in 1..20) {
                addToList("HELLO", "JOJO", R.mipmap.ic_launcher_round_base)
            }
        }
    }

    private val userLikedMusicsListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val user: User? = try {
                dataSnapshot.getValue<User>()
            } catch (e: DatabaseException) {
                null
            }
            if (user != null) {
                val likedMusics: MutableList<MusicMetadata> = user.likedMusics
                for (music in likedMusics) {
                    addToList(music.title, music.artist, R.mipmap.ic_launcher_round_base)
                }
            }
        }


        override fun onCancelled(databaseError: DatabaseError) {
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}