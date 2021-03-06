package ch.epfl.sdp.blindwar.data.music.metadata

import android.net.Uri
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
     * Fetches music metadata from remote source
     *
     * @param query
     */
    suspend fun fetchMusicMetadataSpotify(query: String) {
        remoteSourceApi.fetchSongMetadata(query)
        remoteSourceApi.musicMetadata.observeForever {
            addToRepo(it as ArrayList<MusicMetadata>)
        }
    }

    /**
     * Fetches music metadata from remote source
     *
     */
    fun fetchMusicMetadataDatabase() {
        remoteMusicDatabase.getSongReference().addOnSuccessListener {
            val partial = MutableLiveData<MusicMetadata>()
            val fetched = ArrayList<MusicMetadata>()

            partial.observeForever { meta ->
                fetched.add(meta)
                if (fetched.size == it.items.size) {
                    addToRepo(fetched)
                }
            }

            it.items.forEach { t ->
                val metadata = tokenizeMetadata(t.name)

                t.downloadUrl.addOnSuccessListener { uri ->
                    val uriMetadata = sanitizeMetadata(metadata, uri)
                    // Log.d("SONG : ", uri.toString())
                    partial.postValue(uriMetadata)
                }
            }
        }.addOnFailureListener {
            Log.d("An error occured", it.toString())
        }
    }

    /**
     * Add it to the repository
     *
     * @param it
     */
    private fun addToRepo(it: ArrayList<MusicMetadata>) {
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

    /**
     * Add a capital letter to each word in a string
     *
     * @param string
     * @return
     */
    private fun capitalizeString(string: String): String {
        return string.split(" ")
            .joinToString(" ") { w -> w.lowercase().replaceFirstChar { c -> c.uppercase() } }
    }

    /**
     * Get the metadata of a song from its name
     *
     * @param name
     * @return
     */
    private fun tokenizeMetadata(name: String): List<String> {
        return name.removeSuffix(".mp3")
            .split("-")
            .map { s -> s.trim() }
    }

    /**
     * Clean metadata and create a music metadata with it
     *
     * @param metadata
     * @param uri
     * @return
     */
    private fun sanitizeMetadata(metadata: List<String>, uri: Uri): MusicMetadata {
        return MusicMetadata.createWithURI(
            capitalizeString(metadata[1]),
            capitalizeString(metadata[0]),
            "https://yt3.ggpht.com/ytc/AKedOLQEPKJQm1iJn986SkSpabjJSdcoh8gPxDtHfpCQ=s88-c-k-c0x00ffffff-no-rj",
            0,
            uri.toString()
        )
    }
}