package ch.epfl.sdp.blindwar.database

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.ktx.storage

object MusicDatabase {
    private val storage = Firebase.storage

    // Create a storage reference from our app
    private val storageRef = storage.reference
    private val songsRef = storageRef.child("songs")


    /**
     * Get song references
     *
     * @return database reference of the selected objects
     */
    fun getSongReference(): Task<ListResult> {
        return songsRef.listAll()
    }
}