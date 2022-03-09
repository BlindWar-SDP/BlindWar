package ch.epfl.sdp.blindwar

import android.media.Image

class User (
    val firstName: String,
    val lastName: String,
    val email: String,
    var screenName: String,
    var profilePicture: Image,
    var userStatistics: AppStatistics
        ){


    //function to change screenname (is this necessary?)
    fun changeScreenName(newName: String) {
        screenName = newName
    }

    //function to change profile picture?
    fun changeProfilePicture(newImage: Image) {
        profilePicture = newImage
    }
}