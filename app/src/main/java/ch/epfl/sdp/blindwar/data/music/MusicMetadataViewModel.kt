package ch.epfl.sdp.blindwar.data.music

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.runBlocking

class MusicMetadataViewModel: ViewModel() {
    private val repository: MusicMetadataRepository = MusicMetadataRepository()

    var metadata = MutableLiveData<ArrayList<MusicMetadata>>()

    fun fetchMusicMetadata(query: String): MutableLiveData<ArrayList<MusicMetadata>> {
        runBlocking {
            metadata = repository.fetchMusicMetadataSpotify(query)
        }

        return metadata
    }

}