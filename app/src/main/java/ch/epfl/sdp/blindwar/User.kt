package ch.epfl.sdp.blindwar

import android.media.Image
import android.net.Uri

class User(
    var email: String,
    var userStatistics: AppStatistics,

    var pseudo: String?,
    var firstName: String?,
    var lastName: String?,
    var birthDate: Long?,
var profilePicture: Uri?,
) {
    class Builder(
        private var email: String,
        private var userStatistics: AppStatistics,

        private var pseudo: String? = null,
        private var firstName: String? = null,
        private var lastName: String? = null,
        var birthDate: Long? = null,
        private var profilePicture: Uri? = null,

        ) {
        fun setLastName(name: String) = apply { this.lastName = name }
        fun setFirstName(name: String) = apply { this.firstName = name }
        fun setBirthDate(date: Long) = apply { this.birthDate = date }
//        fun setImage(link: Uri?) = apply {this.profilePicture = link}

        fun build(): User {
            return User(
                email,
                userStatistics,
                pseudo,
                firstName,
                lastName,
                birthDate,
                profilePicture
            )
        }
    }

//    fun registerUser() {}
}
