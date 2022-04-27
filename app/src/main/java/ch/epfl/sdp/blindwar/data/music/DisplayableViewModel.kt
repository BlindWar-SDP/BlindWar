package ch.epfl.sdp.blindwar.data.music

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.blindwar.game.model.Displayable
import ch.epfl.sdp.blindwar.game.viewmodels.PlaylistViewModel
import kotlinx.coroutines.launch

/**
 *
 */
class DisplayableViewModel: ViewModel() {
    private val musicMetadataViewModel: MusicMetadataViewModel =
        MusicMetadataViewModel()

    private val playlistViewModel: PlaylistViewModel =
        PlaylistViewModel()

    val metadata = MutableLiveData<ArrayList<Displayable>>(arrayListOf())

    init {
        viewModelScope.launch{
            musicMetadataViewModel.fetchMusicMetadata("take on me")
            musicMetadataViewModel.metadata.observeForever{
                addToList(it as ArrayList<Displayable>)
            }

            playlistViewModel.playlists.observeForever{
                addToList(it as ArrayList<Displayable>)
            }
        }
    }

    private fun queryMusicMetadata(query: String) {
        musicMetadataViewModel.fetchMusicMetadata(query)
    }

    private fun queryPlaylistMetadata(query: String) {

    }

    fun queryMetadata(query: String) {
        viewModelScope.launch {
            queryMusicMetadata(query)
            queryPlaylistMetadata(query)
        }
    }

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

    fun filterByType(type: Int) {

    }
}