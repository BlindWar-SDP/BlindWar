package ch.epfl.sdp.blindwar.data.music.metadata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MusicMetadataViewModel : ViewModel() {
    private val repository: MusicMetadataRepository =
        MusicMetadataRepository()

    var metadata =
        MutableLiveData<ArrayList<MusicMetadata>>()

    fun fetchMusicMetadata(query: String): MutableLiveData<ArrayList<MusicMetadata>> {
        viewModelScope.launch {
            repository.fetchMusicMetadataSpotify(query)
            repository.fetchMusicMetadataDatabase()
            repository.musicMetadata.observeForever {
                metadata.postValue(it)
            }
        }

        return metadata
    }
}