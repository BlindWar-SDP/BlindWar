package ch.epfl.sdp.blindwar.user

data class User(
    var uid: String = "",
    var email: String = "",
    var userStatistics: AppStatistics = AppStatistics(),
    var pseudo: String = "",
    var firstName: String? = null,
    var lastName: String? = null,
    var birthDate: Long? = 0,
    var profilePicture: String = "",
    var gender: String? = null,
    var description: String? = null
) {

    class Builder(
        private var uid: String = "",
        private var email: String = "",
        private var userStatistics: AppStatistics = AppStatistics(),
        private var pseudo: String = "",
        private var firstName: String? = null,
        private var lastName: String? = null,
        private var birthDate: Long? = null,
        private var profilePicture: String = "",
        private var gender: String? = null,
        private var description: String? = null
    ) {

        fun setUid(uid: String) = apply { this.uid = uid }
        fun setEmail(email: String) = apply { this.email = email }
        fun setStats(stats: AppStatistics) = apply { this.userStatistics = stats }
        fun setPseudo(pseudo: String) = apply { this.pseudo = pseudo }
        fun setFirstName(name: String?) = apply { this.firstName = name }
        fun setLastName(name: String?) = apply { this.lastName = name }
        fun setBirthDate(date: Long?) = apply { this.birthDate = date }
        fun setProfilePicture(imagePath: String) = apply { this.profilePicture = imagePath }
        fun setGender(gender: String) = apply { this.gender = gender }
        fun setDescription(desc: String) = apply { this.description = desc }

        fun fromUser(user: User) = apply {
            this.uid = user.uid
            this.email = user.email
            this.userStatistics = user.userStatistics
            this.pseudo = user.pseudo
            this.firstName = user.firstName
            this.lastName = user.lastName
            this.birthDate = user.birthDate
            this.profilePicture = user.profilePicture
            this.gender = user.gender
            this.description = user.description
        }

        fun build(): User {
            return User(
                uid,
                email,
                userStatistics,
                pseudo,
                firstName,
                lastName,
                birthDate,
                profilePicture,
                gender,
                description
            )
        }
    }

    override fun toString(): String{
        return pseudo
    }
}
