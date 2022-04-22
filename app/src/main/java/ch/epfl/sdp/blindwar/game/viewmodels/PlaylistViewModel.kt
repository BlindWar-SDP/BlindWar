package ch.epfl.sdp.blindwar.game.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.blindwar.data.playlists.PlaylistRepository
import ch.epfl.sdp.blindwar.game.model.Genre
import ch.epfl.sdp.blindwar.game.model.Playlist
import ch.epfl.sdp.blindwar.game.util.Tutorial

class PlaylistViewModel: ViewModel() {
    private var _playlists: List<Playlist> = Tutorial.BASE_PLAYLISTS
    val playlists: MutableLiveData<List<Playlist>>
        get() = MutableLiveData(_playlists)

    private val repository = PlaylistRepository()

    suspend fun searchNamePlaylist(query: String) {
        _playlists = repository.fetchPlaylists(false)
    }

    fun searchFilterPlaylist(genre: Genre) {

    }

}