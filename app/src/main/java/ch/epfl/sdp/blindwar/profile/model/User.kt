package ch.epfl.sdp.blindwar.profile.model

import ch.epfl.sdp.blindwar.data.music.metadata.URIMusicMetadata


import ch.epfl.sdp.blindwar.game.model.GameResult

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

data class User(
    var uid: String = "",
    var email: String = "",
    var userStatistics: AppStatistics = AppStatistics(),
    var pseudo: String = "",
    var firstName: String? = null,
    var lastName: String? = null,
    var birthDate: Long? = 0,
    var profilePicture: String = "",
    var likedMusics: MutableList<URIMusicMetadata> = mutableListOf(),
    var matchHistory: MutableList<GameResult> = mutableListOf(),
    var gender: String = "",
    var description: String = ""
) {

    class Builder(
        private var uid: String = "",
        private var email: String = "",
        private var userStatistics: AppStatistics = AppStatistics(),
        private var pseudo: String = "",
        private var firstName: String = "",
        private var lastName: String = "",
        private var birthDate: Long = -1,
        private var profilePicture: String = "",
        private var likedMusics: MutableList<URIMusicMetadata> = mutableListOf(),
        private var matchHistory: MutableList<GameResult> = mutableListOf(),
        private var gender: String = "",
        private var description: String = ""
    ) {

        fun setUid(uid: String) = apply { this.uid = uid }
        fun setEmail(email: String) = apply { this.email = email }
        fun setStats(stats: AppStatistics) = apply { this.userStatistics = stats }
        fun setPseudo(pseudo: String) = apply { this.pseudo = pseudo }
        fun setFirstName(name: String) = apply { this.firstName = name }
        fun setLastName(name: String) = apply { this.lastName = name }
        fun setBirthdate(date: Long) = apply { this.birthDate = date }
        fun setProfilePicture(imagePath: String) = apply { this.profilePicture = imagePath }
        fun setGender(gender: String) = apply { this.gender = gender }
        fun setDescription(desc: String) = apply { this.description = desc }
        fun setLikedMusics(likedMusics: MutableList<URIMusicMetadata>) = apply { this.likedMusics =
            likedMusics }
        fun matchHistory(matchHistory: MutableList<GameResult>) = apply { this.matchHistory =
            matchHistory }


        fun fromUser(user: User) = apply {
            this.uid = user.uid
            this.email = user.email
            this.userStatistics = user.userStatistics
            this.pseudo = user.pseudo
            this.firstName = user.firstName.toString()
            this.lastName = user.lastName.toString()
            this.birthDate = user.birthDate!!
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
                likedMusics,
                matchHistory,
                gender,
                description
            )
        }
    }

    override fun toString(): String{
        return pseudo
    }
}
