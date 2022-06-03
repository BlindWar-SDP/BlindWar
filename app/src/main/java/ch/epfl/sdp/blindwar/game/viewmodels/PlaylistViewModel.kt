package ch.epfl.sdp.blindwar.game.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.blindwar.data.playlists.PlaylistRepository
import ch.epfl.sdp.blindwar.game.model.Playlist
import kotlinx.coroutines.launch

class PlaylistViewModel : ViewModel() {
    private val _playlists =
        MutableLiveData<ArrayList<Playlist>>(arrayListOf())

    val playlists: MutableLiveData<ArrayList<Playlist>>
        get() = _playlists

    init {
        PlaylistRepository.playlists.observeForever {
            _playlists.postValue(it)
        }
    }

    /**
     * Filter playlist via a query
     *
     */
    fun queryFilterPlaylist() {
        viewModelScope.launch {
            _playlists.postValue(
                //this.value?.filter { it.getName().lowercase().contains(query)} as ArrayList<Playlist>
                PlaylistRepository.playlists.value
            )
        }
    }


    /** TODO: Keep (WIP)
    fun likePlaylist(playlist: Playlist) {}
     **/
}