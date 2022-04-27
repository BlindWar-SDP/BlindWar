package ch.epfl.sdp.blindwar.data.music

import androidx.lifecycle.MutableLiveData
import ch.epfl.sdp.blindwar.data.RemoteMusicMetadataSource
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

    init {
        _musicMetadatas = fetchMusicMetadata()
    }

    private fun fetchMusicMetadata(): MutableLiveData<ArrayList<MusicMetadata>> {
        return MutableLiveData(mockSource.fetchMusicMetadata())
    }

    suspend fun fetchMusicMetadataSpotify(query: String): MutableLiveData<ArrayList<MusicMetadata>> {
        remoteSource.fetchSongMetadata(query)
        return remoteSource.musicMetadata
    }
}