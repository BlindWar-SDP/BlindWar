package ch.epfl.sdp.blindwar.profile.model


import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata
import ch.epfl.sdp.blindwar.game.model.GameResult

data class User(
    var uid: String = "",
    var email: String = "",
    var userStatistics: AppStatistics = AppStatistics(),
    var pseudo: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var birthdate: Long = -1,
    var profilePicture: String = "",
    var likedMusics: MutableList<MusicMetadata> = mutableListOf(),
    var matchHistory: MutableList<GameResult> = mutableListOf(),
    var gender: String = "",
    var description: String = "",
    var matchId: String = ""
) {

    class Builder(
        private var uid: String = "",
        private var email: String = "",
        private var userStatistics: AppStatistics = AppStatistics(),
        private var pseudo: String = "",
        private var firstName: String = "",
        private var lastName: String = "",
        private var birthdate: Long = -1,
        private var profilePicture: String = "",
        private var likedMusics: MutableList<MusicMetadata> = mutableListOf(),
        private var matchHistory: MutableList<GameResult> = mutableListOf(),
        private var gender: String = "",
        private var description: String = "",
        private var matchId: String = ""
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

        /**
         * Create user from another
         *
         * @param user
         * @return
         */
        fun fromUser(user: User) = apply {
            this.uid = user.uid
            this.email = user.email
            this.userStatistics = user.userStatistics
            this.pseudo = user.pseudo
            this.firstName = user.firstName
            this.lastName = user.lastName
            this.birthdate = user.birthdate
            this.profilePicture = user.profilePicture
            this.likedMusics = user.likedMusics
            this.matchHistory = user.matchHistory
            this.gender = user.gender
            this.description = user.description
            this.matchId = user.matchId
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
                likedMusics,
                matchHistory,
                gender,
                description,
                matchId
            )
        }
    }

    /**
     * Return pseudo
     *
     * @return
     */
    override fun toString(): String {
        return pseudo
    }
}
