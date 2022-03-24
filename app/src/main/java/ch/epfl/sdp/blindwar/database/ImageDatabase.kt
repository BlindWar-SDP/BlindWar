package ch.epfl.sdp.blindwar.database

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.net.URI
import java.util.*

class ImageDatabase {

    private val storage = Firebase.storage

    // Create a storage reference from our app
    private val storageRef = storage.reference
    private val imagesRef = storageRef.child("images")



    fun uploadImage(imageURI: Uri, view: View? = null): String {
        val randomKey = UUID.randomUUID().toString()

        // Create a reference to the image to upload
        val uploadedImageRef = imagesRef.child(randomKey)
        if (view != null) {
            uploadedImageRef.putFile(imageURI)
                .addOnSuccessListener {
                    Snackbar.make(view, "Image uploaded", Snackbar.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(getApplicationContext(), "Failed to upload file",
                        Toast.LENGTH_LONG).show()
                }
        }
        else {
            uploadedImageRef.putFile(imageURI)
        }
        return uploadedImageRef.path
    }


}
