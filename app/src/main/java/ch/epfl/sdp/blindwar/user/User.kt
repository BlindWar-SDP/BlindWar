package ch.epfl.sdp.blindwar.user

data class User(
    var email: String = "",
    var userStatistics: AppStatistics = AppStatistics(),
    var pseudo: String = "",
    var firstName: String? = null,
    var lastName: String? = null,
    var birthDate: Long? = 0,
    var profilePicture: String? = null
) {

    class Builder(
        private var email: String = "",
        private var userStatistics: AppStatistics = AppStatistics(),
        private var pseudo: String = "",
        private var firstName: String? = null,
        private var lastName: String? = null,
        private var birthDate: Long? = null,
        private var profilePicture: String? = null,
    ) {
        fun setEmail(email: String) = apply { this.email = email }
        fun setStats(stats: AppStatistics) = apply { this.userStatistics = stats }
        fun setPseudo(pseudo: String) = apply { this.pseudo = pseudo }
        fun setFirstName(name: String?) = apply { this.firstName = name }
        fun setLastName(name: String?) = apply { this.lastName = name }
        fun setBirthDate(date: Long?) = apply { this.birthDate = date }
        fun setProfilePicture(imagePath: String) = apply { this.profilePicture = imagePath }

        fun fromUser(user: User) = apply {
            this.email = user.email
            this.userStatistics = user.userStatistics
            this.pseudo         = user.pseudo
            this.firstName      = user.firstName
            this.lastName       = user.lastName
            this.birthDate      = user.birthDate
            this.profilePicture = user.profilePicture
        }

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
}
