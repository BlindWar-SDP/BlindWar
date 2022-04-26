package ch.epfl.sdp.blindwar.profile.model

import kotlinx.serialization.Serializable

//const val UID = "uid"
//const val EMAIL = "email`"
//const val STATS = "userStatistics"
//const val PSEUDO = "pseudo"
//const val FIRSTNAME = "firstName"
//const val LASTNAME = "lastName"
//const val BIRTHDATE = "birthdate"
//const val PROFILE_PICTURE = "profilePicture"
//const val GENDER = "gender"
//const val DESCRIPTION = "description"

@Serializable
data class User(
    var uid: String = "",
    var email: String = "",
    var userStatistics: AppStatistics = AppStatistics(),
    var pseudo: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var birthdate: Long = -1,
    var profilePicture: String = "",
    var gender: String = "",
    var description: String = ""
) {

    enum class Gender {
        Other, Female, Male, Undefined, None
    }

    enum class VarName {
        uid, email, userStatistics, pseudo, firstName, lastName, birthdate, profilePicture, gender, description, user
    }

    class Builder(
        private var uid: String = "",
        private var email: String = "",
        private var userStatistics: AppStatistics = AppStatistics(),
        private var pseudo: String = "",
        private var firstName: String = "",
        private var lastName: String = "",
        private var birthdate: Long = -1,
        private var profilePicture: String = "",
        private var gender: String = "",
        private var description: String = ""
    ) {

        fun setUid(uid: String) = apply { this.uid = uid }
        fun setEmail(email: String) = apply { this.email = email }
        fun setStats(stats: AppStatistics) = apply { this.userStatistics = stats }
        fun setPseudo(pseudo: String) = apply { this.pseudo = pseudo }
        fun setFirstName(name: String) = apply { this.firstName = name }
        fun setLastName(name: String) = apply { this.lastName = name }
        fun setBirthdate(date: Long) = apply { this.birthdate = date }
        fun setProfilePicture(imagePath: String) = apply { this.profilePicture = imagePath }
        fun setGender(gender: String) = apply { this.gender = gender }
        fun setDescription(desc: String) = apply { this.description = desc }

        fun fromUser(user: User) = apply {
            setUid(user.uid)
            setEmail(user.email)
            setStats(user.userStatistics)
            setPseudo(user.pseudo)
            setFirstName(user.firstName)
            setLastName(user.lastName)
            setBirthdate(user.birthdate)
            setProfilePicture(user.profilePicture)
            setGender(user.gender)
            setDescription(user.description)
        }

        fun build(): User {
            return User(
                uid,
                email,
                userStatistics,
                pseudo,
                firstName,
                lastName,
                birthdate,
                profilePicture,
                gender,
                description
            )
        }
    }

    override fun toString(): String {
        return pseudo
    }
}
