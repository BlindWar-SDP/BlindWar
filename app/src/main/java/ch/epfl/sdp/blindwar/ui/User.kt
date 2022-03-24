package ch.epfl.sdp.blindwar.ui

import android.net.Uri

data class User(
    val email: String,
    val userStatistics: AppStatistics,
    val pseudo: String?,
    val firstName: String?,
    val lastName: String?,
    val birthDate: Long?,
    val profilePicture: Uri?,
) {
    fun builder(): UserBuilder {
        return UserBuilder().setBirthDate(birthDate!!)
            .setEmail(email)
            .setFirstName(firstName!!)
            .setLastName(lastName!!)
            .setPseudo(pseudo!!)
            .setStats(userStatistics)
            .setImage(profilePicture)
    }
}

class UserBuilder(
    private var email: String = "",
    private var userStatistics: AppStatistics = AppStatistics(),
    private var pseudo: String? = null,
    private var firstName: String? = null,
    private var lastName: String? = null,
    var birthDate: Long? = null,
    private var profilePicture: Uri? = null,
) {
    fun setEmail(email: String) = apply { this.email = email }
    fun setStats(stats: AppStatistics) = apply { this.userStatistics = stats }
    fun setLastName(name: String) = apply { this.lastName = name }
    fun setFirstName(name: String) = apply { this.firstName = name }
    fun setBirthDate(date: Long) = apply { this.birthDate = date }
    fun setImage(link: Uri?) = apply { this.profilePicture = link }
    fun setPseudo(pseudo: String) = apply { this.pseudo = pseudo }

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