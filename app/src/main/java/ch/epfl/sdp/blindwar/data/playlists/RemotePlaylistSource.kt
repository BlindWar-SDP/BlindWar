package ch.epfl.sdp.blindwar.data.playlists

import ch.epfl.sdp.blindwar.game.model.Playlist
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RemotePlaylistSource(
    private val ioDispatcher: CoroutineDispatcher
) {
    // Need to adapt this code with constraint
    suspend fun fetchPlaylists(): List<Playlist> =
        withContext(ioDispatcher) {
            // Fetch playlists from database
            ArrayList()
        }
}