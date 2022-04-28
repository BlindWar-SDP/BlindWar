package ch.epfl.sdp.blindwar.database

import ch.epfl.sdp.blindwar.game.model.OnlinePlaylist
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object PlaylistDatabase {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val playlistReference = database.getReference("Playlists")

    /**
     * Get playlist reference to manipulate playlist infos
     *
     * @param pid playlist identification
     * @return playlist reference
     */
    private fun getPlaylistReference(pid: String): DatabaseReference {
        return PlaylistDatabase.playlistReference.child(pid)
    }

    /**
     * Function to add an playlist to the database
     *
     * @param onlinePlaylist to be added
     */
    fun addPlaylist(onlinePlaylist: OnlinePlaylist) {
        playlistReference.child(onlinePlaylist.uid).setValue(onlinePlaylist)
    }
}