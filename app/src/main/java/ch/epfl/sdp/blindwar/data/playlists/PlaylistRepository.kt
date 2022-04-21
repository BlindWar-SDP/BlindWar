package ch.epfl.sdp.blindwar.data.playlists

import ch.epfl.sdp.blindwar.game.model.Playlist
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class PlaylistRepository(
    private val remotePlaylistSource: RemotePlaylistSource
) {
    // Mutex to make writes to cached values thread-safe.
    private val playlistsMutex = Mutex()

    // Cache of the latest news got from the network.
    private var playlists: List<Playlist> = emptyList()

    // Adapt to a searchable database
    suspend fun fetchPlaylists(refresh: Boolean = false): List<Playlist> {
        if (refresh || playlists.isEmpty()) {
            val networkResult = remotePlaylistSource.fetchPlaylists()
            // Thread-safe write to latestNews
            playlistsMutex.withLock {
                this.playlists = networkResult
            }
        }

        return playlistsMutex.withLock { this.playlists }
    }
}


