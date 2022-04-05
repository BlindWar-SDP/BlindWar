package ch.epfl.sdp.blindwar.ui

import android.content.ContentValues.TAG
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.data.*
import com.squareup.picasso.Picasso
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import kotlin.random.Random

class PlaylistCreationActivity: AppCompatActivity() {
    private var logged: Boolean = false
    private lateinit var responseToken: SpotifyToken
    private lateinit var coverView: ImageView
    private lateinit var player: MediaPlayer
    private lateinit var artistName: TextView
    private lateinit var trackName: TextView
    private lateinit var search: EditText
    private lateinit var searchButton: Button
    private var index = 0

    private val appleMusicsongUrl: String =
        "https://p.scdn.co/mp3-preview/0aec695bf2d68f2c378d8acd640327104a6da336?cid=774b29d4f13844c495f206cafdad9c86"
    private lateinit var playlist: MutableList<String>
    private lateinit var playlistUrl: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify)
        coverView = findViewById(R.id.trackCover)
        search = findViewById(R.id.searchText)
        trackName = findViewById(R.id.trackNames)
        artistName = findViewById(R.id.artistName)

        playlist = mutableListOf("Silver for monsters", "steel for human", "haine et sex", "darude")
        playlistUrl = mutableListOf()

        responseToken = SpotifyToken("", 0, "")
        player = MediaPlayer()

        searchButton = findViewById<Button>(R.id.searchButton).also{ button ->
            button.setOnClickListener{
                search.onEditorAction(EditorInfo.IME_ACTION_DONE)
                playlist.add(search.text.toString())
            }
        }

        findViewById<ConstraintLayout>(R.id.layoutSpot).setOnClickListener{
            search.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }

        fetchToken(playlist[0])
    }

    private fun fetchToken(songName: String) {
        lifecycleScope.launchWhenCreated{
            if (!logged) {
                val auth = try {
                    SpotifyService.apiAuth.value.getToken()
                } catch (e: IOException) {
                    return@launchWhenCreated
                } catch (e: HttpException) {
                    return@launchWhenCreated
                } catch (e: Exception) {
                    return@launchWhenCreated
                }

                if (auth.isSuccessful && auth.body() != null) {
                    Log.d(TAG, responseToken.toString())
                    responseToken = auth.body()!!
                    logged = true
                    Log.d(TAG, "AUTHENTICATION SUCCESSFUL")
                    Log.d(TAG, responseToken.access_token)
                    searchTrack(songName)
                } else {
                    Log.d(
                        TAG,
                        "AUTHENTICATION NOT SUCCESSFUL ${
                            auth.code().toString() + auth.message() + auth.toString()
                        }"
                    )
                }
            } else {
                searchTrack(songName)
            }
        }
    }

    private fun searchTrack(songName: String) {
        var response: Response<SpotifySearchTrackResult>
        lifecycleScope.launchWhenCreated {
            if (logged) {
                response = try {
                    SpotifyService.apiMeta.value.searchTrack(token = "Bearer ${responseToken.access_token}",
                        query = songName
                    )
                } catch(e: IOException) {
                    Log.e(TAG, "IOException, you might not have internet connection")
                    logged = false
                    return@launchWhenCreated
                } catch (e: HttpException) {
                    Log.e(TAG, "HttpException, unexpected response")
                    logged = false
                    return@launchWhenCreated
                }

                if (response.isSuccessful
                    && response.body() != null
                    && response.body()!!.tracks.items.isNotEmpty()) {

                    val mp3 = response.body()!!.tracks.items[0].preview_url
                    if (mp3 != null) {
                        if (player.isPlaying) {
                            player.pause()
                        }

                        val imageUrl = response.body()!!.tracks.items[0].album.images[1]
                        Picasso.get().load(imageUrl.url).into(coverView)
                        trackName.text = response.body()!!.tracks.items[0].name
                        artistName.text = response.body()!!.tracks.items[0].artists[0].name
                        player = MediaPlayer()
                        val url = response.body()!!.tracks.items[0].preview_url
                        player.setDataSource(url)
                        player.setOnCompletionListener {
                            index = randomIndex()
                            fetchToken(playlist[index])
                        }
                        player.prepare()
                        player.start()
                    }

                    //songMetaData = spotifyArtistToSongMetadata(response.body()!!)
                } else {
                    index = randomIndex()
                    fetchToken(playlist[index])
                    Log.e(TAG, "Response not successful")
                }
            }
        }
    }

    private fun randomIndex(): Int {
        return if (playlist.size > 1) {
            var random = Random.nextInt(playlist.size)
            while (random == index) {
                random = Random.nextInt(playlist.size)
            }
            random

        } else
            index
    }
}