package ch.epfl.sdp.blindwar.data.music.metadata

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import ch.epfl.sdp.blindwar.data.spotify.SpotifyService.apiAuth
import ch.epfl.sdp.blindwar.data.spotify.SpotifyService.apiMeta
import ch.epfl.sdp.blindwar.database.MusicDatabase
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.descriptors.PrimitiveKind
import java.net.URI

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
        remoteSourceApi.musicMetadata.observeForever{
            addToRepo(it as ArrayList<URIMusicMetadata>)
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
                    addToRepo(fetched)
                }
            }

            it.items.forEach{ t ->
                val metadata = tokenizeMetadata(t.name)

                t.downloadUrl.addOnSuccessListener { uri ->
                    val uriMetadata = sanitizeMetadata(metadata, uri)
                    // Log.d("SONG : ", uri.toString())
                    partial.postValue(uriMetadata)
                }
            }
        }.addOnFailureListener{
            Log.d("An error occured", it.toString())
        }
    }

    private fun addToRepo(it: ArrayList<URIMusicMetadata>) {
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

    /** Put these functions in string util **/
    private fun capitalizeString(string: String): String {
        return string.split(" ").joinToString(" ") { w -> w.lowercase().replaceFirstChar {c -> c.uppercase()}}
    }

    private fun tokenizeMetadata(name: String): List<String> {
        return name.removeSuffix(".mp3")
            .split("-")
            .map{s -> s.trim()}
    }

    private fun sanitizeMetadata(metadata: List<String>, uri: Uri): URIMusicMetadata {
        return URIMusicMetadata(
            artist = capitalizeString(metadata[0]),
            title = capitalizeString(metadata[1]),
            imageUrl = "https://yt3.ggpht.com/ytc/AKedOLQEPKJQm1iJn986SkSpabjJSdcoh8gPxDtHfpCQ=s88-c-k-c0x00ffffff-no-rj",
            uri = uri.toString())
    }
}