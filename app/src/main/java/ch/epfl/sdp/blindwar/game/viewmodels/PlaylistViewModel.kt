package ch.epfl.sdp.blindwar.game.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.blindwar.data.playlists.PlaylistRepository
import ch.epfl.sdp.blindwar.game.model.Genre
import ch.epfl.sdp.blindwar.game.model.OnlinePlaylist
import ch.epfl.sdp.blindwar.game.model.Playlist
import kotlinx.coroutines.launch

class PlaylistViewModel: ViewModel() {
    private val _playlists =
        MutableLiveData<ArrayList<Playlist>>(arrayListOf())

    var playlists: MutableLiveData<ArrayList<Playlist>> = MutableLiveData()
        get() = _playlists

    init {
        PlaylistRepository.playlists.observeForever{
            _playlists.postValue(it)
        }
    }

    fun queryFilterPlaylist(query: String) {
        viewModelScope.launch{
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