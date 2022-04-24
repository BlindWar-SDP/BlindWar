package ch.epfl.sdp.blindwar.game.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.blindwar.data.playlists.PlaylistRepository
import ch.epfl.sdp.blindwar.game.model.Genre
import ch.epfl.sdp.blindwar.game.model.OnlinePlaylist
import ch.epfl.sdp.blindwar.game.model.Playlist

class PlaylistViewModel: ViewModel() {
    private val _playlists: MutableLiveData<ArrayList<Playlist>> = PlaylistRepository.playlists

    var playlists: MutableLiveData<ArrayList<Playlist>> = MutableLiveData()
        get() = _playlists

    fun searchFilterPlaylist(genre: Genre) {

    }

    fun likePlaylist(playlist: Playlist) {

    }
}