package ch.epfl.sdp.blindwar.database

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.*

object ImageDatabase {

    private val storage = Firebase.storage

    // Create a storage reference from our app
    private val storageRef = storage.reference
    private val imagesRef = storageRef.child("images")


/*
    fun uploadImage(imageURI: Uri, view: View? = null): String {
        val randomKey = UUID.randomUUID().toString()

        // Create a reference to the image to upload
        val uploadedImageRef = imagesRef.child(randomKey)
        if (view != null) {
            uploadedImageRef.putFile(imageURI)
                .addOnSuccessListener {
                    Snackbar.make(view, "Image uploaded", Snackbar.LENGTH_LONG).show()
                }
            /*
                .addOnFailureListener {
                    Toast.makeText(getApplicationContext(), "Failed to upload file",
                        Toast.LENGTH_LONG).show()
                }*/
        } else {
            uploadedImageRef.putFile(imageURI)
        }
        return uploadedImageRef.path
    } */

    fun uploadProfilePicture(imageURI: Uri): String {
        val randomKey = UUID.randomUUID().toString()

        // Create a reference to the image to upload
        val uploadedImageRef = imagesRef.child(randomKey)
        uploadedImageRef.putFile(imageURI)
        return uploadedImageRef.path
    }


    fun downloadProfilePicture(imagePath: String, imageView: ImageView, context: Context): String {
        val profilePictureRef = storageRef.child(imagePath)
        GlideApp.with(context)
            .load(profilePictureRef)
            .centerCrop()
            .into(imageView)
        return profilePictureRef.path
    }

    fun getImageReference(imagePath: String): StorageReference {
        return storageRef.child(imagePath)
    }

}




