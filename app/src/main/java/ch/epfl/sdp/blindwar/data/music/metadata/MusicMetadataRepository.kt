package ch.epfl.sdp.blindwar.data.music.metadata

import android.util.Log
import androidx.lifecycle.MutableLiveData
import ch.epfl.sdp.blindwar.data.spotify.SpotifyService.apiAuth
import ch.epfl.sdp.blindwar.data.spotify.SpotifyService.apiMeta
import ch.epfl.sdp.blindwar.database.MusicDatabase
import kotlinx.coroutines.Dispatchers

class MusicMetadataRepository {
    private var _musicMetadatas: MutableLiveData<ArrayList<MusicMetadata>> =
        MutableLiveData(arrayListOf())

    private val remoteSourceApi: RemoteMusicMetadataSource = RemoteMusicMetadataSource(
        apiMeta.value,
        apiAuth.value,
        Dispatchers.IO
    )

    private val remoteMusicDatabase = MusicDatabase

    val musicMetadata: MutableLiveData<ArrayList<MusicMetadata>>
        get() = _musicMetadatas

    /**
     * Fetches music metadata from local mock source
     *

    private fun fetchMusicMetadata(): MutableLiveData<ArrayList<MusicMetadata>> {
        return MutableLiveData(ch.epfl.sdp.blindwar.data.MockMusicdataSource.fetchMusicMetadata())
    }
    **/

    /**
     * Fetches music metadata from remote source
     *
     * @param query
     */
    suspend fun fetchMusicMetadataSpotify(query: String) {
        remoteSourceApi.fetchSongMetadata(query)
        remoteSourceApi.musicMetadata.observeForever{
            addToList(it as ArrayList<URIMusicMetadata>)
        }
    }

    /**
     * Fetches music metadata from remote source
     *
     */
    fun fetchMusicMetadataDatabase() {
        remoteMusicDatabase.getSongReference().addOnSuccessListener {
            val partial = MutableLiveData<URIMusicMetadata>()
            val fetched = ArrayList<URIMusicMetadata>()

            partial.observeForever{ meta ->
                fetched.add(meta)
                if (fetched.size == it.items.size) {
                    addToList(fetched)
                    /**
                    PlaylistDatabase.addPlaylist(
                        OnlinePlaylist("Shittyflute",
                            "Shittyfluted",
                            "BlindWar",
                            arrayListOf(Genre.POP),
                            fetched,
                            "https://yt3.ggpht.com/ytc/AKedOLQEPKJQm1iJn986SkSpabjJSdcoh8gPxDtHfpCQ=s88-c-k-c0x00ffffff-no-rj",
                            GameUtil.URL_PREVIEW_FIFA
                        )
                    )**/
                }
            }

            it.items.forEach{ t ->
                val metadata = t.name.removeSuffix(".mp3")
                    .split("-")
                    .map{s -> s.trim()}

                t.downloadUrl.addOnSuccessListener { uri ->
                    val uriMetadata = URIMusicMetadata(
                        artist = metadata[0].split(" ").joinToString(" ") { w -> w.lowercase().replaceFirstChar {c -> c.uppercase()}},
                        title = metadata[1].split(" ").joinToString (" "){ w -> w.lowercase().replaceFirstChar { c -> c.uppercase()}},
                        imageUrl = "https://yt3.ggpht.com/ytc/AKedOLQEPKJQm1iJn986SkSpabjJSdcoh8gPxDtHfpCQ=s88-c-k-c0x00ffffff-no-rj",
                        uri = uri.toString())
                    Log.d("SONG : ", uri.toString())
                    partial.postValue(uriMetadata)
                }
            }
        }.addOnFailureListener{
            Log.d("An error occured", it.toString())
        }
    }

    private fun addToList(it: ArrayList<URIMusicMetadata>) {
        if (!it.isNullOrEmpty()) {
            _musicMetadatas.postValue(
                _musicMetadatas.value.let { value ->
                    val copy = ArrayList(value)
                    copy.addAll(it)
                    copy
                }
            )
        }
    }
}