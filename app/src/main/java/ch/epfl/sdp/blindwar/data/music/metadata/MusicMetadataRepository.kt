package ch.epfl.sdp.blindwar.data.music.metadata

import androidx.lifecycle.MutableLiveData
import ch.epfl.sdp.blindwar.data.music.MockMusicdataSource
import ch.epfl.sdp.blindwar.data.spotify.SpotifyService.apiAuth
import ch.epfl.sdp.blindwar.data.spotify.SpotifyService.apiMeta
import kotlinx.coroutines.Dispatchers

class MusicMetadataRepository {
    private val mockSource: MockMusicdataSource = MockMusicdataSource
    private var _musicMetadatas: MutableLiveData<ArrayList<MusicMetadata>> =
        MutableLiveData(arrayListOf())

    private val remoteSource: RemoteMusicMetadataSource = RemoteMusicMetadataSource(
        apiMeta.value,
        apiAuth.value,
        Dispatchers.IO
    )

    val musicMetadata: MutableLiveData<ArrayList<MusicMetadata>>
        get() = _musicMetadatas


    /**
     * Fetches music metadata from local mock source
     *
     */
    private fun fetchMusicMetadata(): MutableLiveData<ArrayList<MusicMetadata>> {
        return MutableLiveData(MockMusicdataSource.fetchMusicMetadata())
    }

    /**
     * Fetches music metadata from remote source
     *
     * @param query
     */
    suspend fun fetchMusicMetadataSpotify(query: String) {
        remoteSource.fetchSongMetadata(query)
        remoteSource.musicMetadata.observeForever{
            _musicMetadatas.postValue(it)
        }
    }
}