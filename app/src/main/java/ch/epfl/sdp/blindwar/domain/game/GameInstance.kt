package ch.epfl.sdp.blindwar.domain.game

data class GameInstance(
    val gameConfig: GameConfig,
    val playlist: List<SongMetaData>,
    //val record: List<SongInstance> // Added at each round of the game
)

data class GameConfig(
    val difficulty: GameDifficulty,
    val format: GameFormat,
    val mode: GameMode,
    val parameter: GameParameter
)

data class GameParameter(
    val round: Int,
    val funny: Boolean,
    val hint: Boolean,
    val timeToFind: Int
)

enum class GameDifficulty {
    EASY,
    MEDIUM,
    DIFFICULT,
    SHAZAM
}

enum class GameFormat {
    SOLO,
    TEAM
}

enum class GameMode {
    REGULAR,
    SURVIVAL,
    TIMED
}