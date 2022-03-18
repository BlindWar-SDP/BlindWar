package ch.epfl.sdp.blindwar.ui

import android.media.Image

class User(
    val firstName: String = "firstName",
    val lastName: String = "lastName",
    val email: String = "email",
    var screenName: String = "screenName",
    //var profilePicture: Image?,
    var userStatistics: AppStatistics = AppStatistics()) {

}


