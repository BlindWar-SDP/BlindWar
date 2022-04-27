package ch.epfl.sdp.blindwar.data.music

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MusicMetadataViewModel: ViewModel() {
    private val repository: MusicMetadataRepository =
        MusicMetadataRepository()

    var metadata =
        MutableLiveData<ArrayList<MusicMetadata>>()

    fun fetchMusicMetadata(query: String): MutableLiveData<ArrayList<MusicMetadata>> {
        viewModelScope.launch {
            repository.fetchMusicMetadataSpotify(query)
            repository.musicMetadata.observeForever{
                metadata.postValue(it)
            }
        }

        return metadata
    }
}