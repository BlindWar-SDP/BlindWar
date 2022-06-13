@file:Suppress("ControlFlowWithEmptyBody")

package ch.epfl.sdp.blindwar.database

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.*


object ImageDatabase {
    private val storage = Firebase.storage

    // Create a storage reference from our app
    private val storageRef = storage.reference
    private val imagesRef = storageRef.child("images")

    /**
     * Upload the profilePicture from uri
     *
     * @param imageURI
     * @return
     */
    fun uploadProfilePicture(imageURI: Uri): String {
        val randomKey = UUID.randomUUID().toString()
        // Create a reference to the image to upload
        val uploadedImageRef = imagesRef.child(randomKey)
        val uploadTask = uploadedImageRef.putFile(imageURI)
        while (!uploadTask.isComplete);// TODO : Bad practice, waste cycles
        return uploadedImageRef.path
    }

    /**
     * Get the storageReference of an image from a path
     *
     * @param imagePath
     * @return
     */
    fun getImageReference(imagePath: String): StorageReference {
        return storageRef.child(imagePath)
    }
}




