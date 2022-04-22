package ch.epfl.sdp.blindwar.data.playlists

import androidx.lifecycle.MutableLiveData
import ch.epfl.sdp.blindwar.game.model.Playlist
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemotePlaylistSource() {
    private val playlistReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Playlists")

    suspend fun fetchPlaylists(): MutableLiveData<PlaylistResponse> {
        val mutableLiveData = MutableLiveData<PlaylistResponse>()
        withContext(Dispatchers.IO) {
            playlistReference.get().addOnCompleteListener { task ->
                val response = PlaylistResponse()
                if (task.isSuccessful) {
                    val result = task.result
                    result?.let {
                        response.playlists = result.children.map { snapShot ->
                            snapShot.getValue(Playlist::class.java)!!
                        }
                    }
                } else {
                    response.exception = task.exception
                }
                mutableLiveData.value = response
            }
        }

        return mutableLiveData
    }
}