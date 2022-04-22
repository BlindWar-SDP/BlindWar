package ch.epfl.sdp.blindwar.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object PlaylistDatabase {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val playlistReference = database.getReference("Playlists")


    /**
     * Get user reference to manipulate playlist infos
     *
     * @param pid playlist identification
     * @return
     */
    private fun getPlaylistReference(uid: String): DatabaseReference {
        return PlaylistDatabase.playlistReference.child(uid)
    }

    


}