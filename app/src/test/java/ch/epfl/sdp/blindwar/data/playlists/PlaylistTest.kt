package ch.epfl.sdp.blindwar.data.playlists

import ch.epfl.sdp.blindwar.game.model.*
import junit.framework.TestCase

class PlaylistTest : TestCase() {

    fun testPlaylistCreation() {
        val playlist = Playlist()
        assertTrue(playlist.imageUrl.isEmpty())
        assertTrue(playlist.uid.isEmpty())
        assertTrue(playlist.difficulty == null)
        assertTrue(playlist.genres.isEmpty())
        assertTrue(playlist.songs.isEmpty())
    }

    fun testLocalPlaylistCreation() {
        val playlist = LocalPlaylist("", "")
        assertTrue(playlist.imageUrl.isEmpty())
        assertTrue(playlist.uid.isEmpty())
        assertTrue(playlist.difficulty == null)
        assertTrue(playlist.genres.isEmpty())
        assertTrue(playlist.songs.isEmpty())
    }

    fun testOnlinePlaylistCreation() {
        val playlist = OnlinePlaylist("", "")
        assertTrue(playlist.imageUrl.isEmpty())
        assertTrue(playlist.uid.isEmpty())
        assertTrue(playlist.difficulty == null)
        assertTrue(playlist.genres.isEmpty())
        assertTrue(playlist.songs.isEmpty())
    }
}