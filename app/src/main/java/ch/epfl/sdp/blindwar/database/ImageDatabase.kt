package ch.epfl.sdp.blindwar.database

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
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

    fun uploadProfilePicture(user: FirebaseUser?, imageURI: Uri, view: View? = null): String {
        val randomKey = UUID.randomUUID().toString()

        // Create a reference to the image to upload
        val uploadedImageRef = imagesRef.child(randomKey)
        if (view != null) {
            uploadedImageRef.putFile(imageURI)
                .addOnSuccessListener {
                    Snackbar.make(view, "Image uploaded", Snackbar.LENGTH_LONG).show()
                    UserDatabase.addProfilePicture(user!!.uid, uploadedImageRef.path)
                }
            /*
        .addOnFailureListener {
            Toast.makeText(getApplicationContext(), "Failed to upload file",
                Toast.LENGTH_LONG).show()
        }*/
        }
        else {
            uploadedImageRef.putFile(imageURI)
        }
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

}




