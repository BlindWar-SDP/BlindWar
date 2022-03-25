package ch.epfl.sdp.blindwar.ui

import android.net.Uri

data class User(
    val email: String = "email",
    val userStatistics: AppStatistics = AppStatistics(),
    val pseudo: String? = "pseudo",
    val firstName: String? = "firstName",
    val lastName: String? = "lastName",
    val birthDate: Long? = 0,
    val profilePicture: String? = "",
) {
    fun builder(): UserBuilder {
        return UserBuilder().setBirthDate(birthDate!!)
            .setEmail(email)
            .setFirstName(firstName!!)
            .setLastName(lastName!!)
            .setPseudo(pseudo!!)
            .setStats(userStatistics)
            .setImage(profilePicture!!)
    }
}

class UserBuilder(
    private var email: String = "email",
    private var userStatistics: AppStatistics = AppStatistics(),
    private var pseudo: String? = "pseudo",
    private var firstName: String? = "firstName",
    private var lastName: String? = "lastName",
    private var birthDate: Long? = 0,
    private var profilePicture: String? = "",
    ) {
    fun setEmail(email:String) = apply {this.email = email}
    fun setStats(stats: AppStatistics) = apply {this.userStatistics = stats}
    fun setLastName(name: String) = apply { this.lastName = name }
    fun setFirstName(name: String) = apply { this.firstName = name }
    fun setBirthDate(date: Long) = apply { this.birthDate = date }
    fun setImage(link: String?) = apply {this.profilePicture = link}

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
