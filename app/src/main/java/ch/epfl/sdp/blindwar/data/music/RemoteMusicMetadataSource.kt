package ch.epfl.sdp.blindwar.data

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ch.epfl.sdp.blindwar.data.music.MusicMetadata
import ch.epfl.sdp.blindwar.data.music.URIMusicMetadata
import ch.epfl.sdp.blindwar.data.spotify.SpotifyApi
import ch.epfl.sdp.blindwar.data.spotify.SpotifyArtist
import ch.epfl.sdp.blindwar.data.spotify.SpotifyService.apiMeta
import ch.epfl.sdp.blindwar.data.spotify.SpotifyToken
import ch.epfl.sdp.blindwar.data.spotify.SpotifyTrack
import ch.epfl.sdp.blindwar.game.util.Tutorial
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class RemoteMusicMetadataSource(private val songMetadataSource: SpotifyApi = apiMeta.value,
                                private val tokenSource: SpotifyApi,
                                private val ioDispatcher: CoroutineDispatcher) {

    private var token = SpotifyToken("", 3600, "")
    private var logged: Boolean = false
    val musicMetadata = MutableLiveData<ArrayList<MusicMetadata>>()

    suspend fun fetchSongMetadata(trackName: String = "take on me") {
        var songMetaData = URIMusicMetadata("", "", "")
        withContext(ioDispatcher) {
            var spotifyTrack: SpotifyTrack
            if (!logged)
                fetchToken()

            if (logged) {
                val response = try {
                    songMetadataSource.searchTrack("Bearer ${token.access_token}", query = trackName)
                } catch(e: IOException) {
                    Log.e(ContentValues.TAG, "IOException, you might not have internet connection")
                    logged = false
                    return@withContext
                } catch (e: HttpException) {
                    Log.e(ContentValues.TAG, "HttpException, unexpected response")
                    logged = false
                    return@withContext
                }

                if (response.isSuccessful && response.body() != null) {

                    //val tracks: List<URIMusicMetadata> =
                    val tracks = (response.body()!!.tracks.items as ArrayList<SpotifyTrack>).map{
                        URIMusicMetadata(
                        it.name,
                        it.artists[0].name,
                        it.album.images[0].url,
                        duration = 30000,
                        uri = if (it.preview_url != null) it.preview_url!! else Tutorial.URL_FIFA_SONG_2)
                    }

                    musicMetadata.postValue(tracks as ArrayList<MusicMetadata>)

                } else {
                    Log.e(ContentValues.TAG, "Response not successful")
                }
            }
        }
    }

    private suspend fun fetchToken() {
        /* Get access token */
        withContext(ioDispatcher) {
            if (!logged) {
                val auth = try {
                    tokenSource.getToken()
                } catch (e: Exception) {
                    Log.d(ContentValues.TAG, e.stackTraceToString())
                    return@withContext
                }

                if (auth.isSuccessful && auth.body() != null) {
                    Log.d(ContentValues.TAG, token.toString())
                    token = auth.body()!!
                    logged = true
                    Log.d(ContentValues.TAG, "AUTHENTICATION SUCCESSFUL")
                    Log.d(ContentValues.TAG, token.access_token)
                } else {
                    Log.d(ContentValues.TAG, "AUTHENTICATION NOT SUCCESSFUL ${auth.code().toString()+auth.message()+auth.toString()}")
                }
            }
        }
    }
}