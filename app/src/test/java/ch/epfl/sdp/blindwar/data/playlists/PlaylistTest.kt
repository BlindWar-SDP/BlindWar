package ch.epfl.sdp.blindwar.data.playlists

import ch.epfl.sdp.blindwar.game.model.Genre
import ch.epfl.sdp.blindwar.game.model.LocalPlaylist
import ch.epfl.sdp.blindwar.game.model.OnlinePlaylist
import ch.epfl.sdp.blindwar.game.model.Playlist
import junit.framework.TestCase

class PlaylistTest : TestCase() {

    fun testPlaylistCreation() {
        val playlist = Playlist()
        val genreList = listOf(Genre.CLASSIC)
        val playlist2 = Playlist("", "", "", genreList)
        assertTrue(playlist.imageUrl.isEmpty())
        assertTrue(playlist.uid.isEmpty())
        assertTrue(playlist.difficulty == null)
        assertTrue(playlist.genres.isEmpty())
        assertEquals(playlist2.getGenre(), "CLASSIC")
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