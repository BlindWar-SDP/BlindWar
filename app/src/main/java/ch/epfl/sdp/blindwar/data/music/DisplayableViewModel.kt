package ch.epfl.sdp.blindwar.data.music

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadataViewModel
import ch.epfl.sdp.blindwar.game.model.Displayable
import ch.epfl.sdp.blindwar.game.viewmodels.PlaylistViewModel
import kotlinx.coroutines.launch

class DisplayableViewModel : ViewModel() {
    private val musicMetadataViewModel: MusicMetadataViewModel =
        MusicMetadataViewModel()

    private val playlistViewModel: PlaylistViewModel =
        PlaylistViewModel()

    val metadata = MutableLiveData<ArrayList<Displayable>>(arrayListOf())

    init {
        viewModelScope.launch {
            musicMetadataViewModel.fetchMusicMetadata("take on me")
            @Suppress("UNCHECKED_CAST")
            setObservable(musicMetadataViewModel.metadata as MutableLiveData<ArrayList<Displayable>>)
            @Suppress("UNCHECKED_CAST")
            setObservable(playlistViewModel.playlists as MutableLiveData<ArrayList<Displayable>>)
        }
    }

    /**
     * Add observer to livedata
     *
     * @param liveData
     */
    private fun setObservable(liveData: MutableLiveData<ArrayList<Displayable>>) {
        liveData.observeForever {
            addToList(it as ArrayList<Displayable>)
        }
    }

    /**
     * Query a music metadata
     *
     * @param query
     */
    private fun queryMusicMetadata(query: String) {
        musicMetadataViewModel.fetchMusicMetadata(query)
    }

    /**
     * Query a music metadata in coroutine
     *
     * @param query
     */
    fun queryMetadata(query: String) {
        viewModelScope.launch {
            queryMusicMetadata(query)
        }
    }

    /**
     * Add it argument to the list of metadata(list of displayable)
     *
     * @param it
     */
    private fun addToList(it: ArrayList<Displayable>) {
        if (!it.isNullOrEmpty()) {
            metadata.postValue(
                metadata.value.let { value ->
                    val copy = ArrayList(value)
                    copy.addAll(it)
                    copy
                }
            )
        }
    }
}