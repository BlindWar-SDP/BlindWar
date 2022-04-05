package ch.epfl.sdp.blindwar.domain.game

data class GameInstance(
    val gameConfig: GameConfig,
    val playlist: List<SongMetaData>)

class GameInstanceBuilder() {
    private var playlist: List<SongMetaData> = Tutorial.gameInstance
        .playlist

    private var gameFormat: GameFormat = GameFormat.SOLO

    private var gameParameter: GameParameter = Tutorial.gameInstance
        .gameConfig
        .parameter

    private var gameMode: GameMode = Tutorial.gameInstance
        .gameConfig
        .mode

    fun setPlaylist(playlist: List<SongMetaData>): GameInstanceBuilder {
        this.playlist = playlist
        return this
    }

    fun setFormat(gameFormat: GameFormat): GameInstanceBuilder {
        this.gameFormat = gameFormat
        return this
    }

    fun setParameter(gameParameter: GameParameter): GameInstanceBuilder {
        this.gameParameter = gameParameter
        return this
    }

    fun setMode(gameMode: GameMode): GameInstanceBuilder {
        this.gameMode = gameMode
        return this
    }

    fun setGameInstance(gameInstance: GameInstance): GameInstanceBuilder {
        setPlaylist(gameInstance.playlist)
        setDifficulty(gameInstance.gameConfig.difficulty)
        setFormat(gameInstance.gameConfig.format)
        setParameter(gameInstance.gameConfig.parameter)
        setMode(gameInstance.gameConfig.mode)
        return this
    }

    private var gameDifficulty: GameDifficulty = Tutorial.gameInstance
        .gameConfig
        .difficulty

    fun setDifficulty(gameDifficulty: GameDifficulty): GameInstanceBuilder {
        this.gameDifficulty = gameDifficulty
        return this
    }

    fun build(): GameInstance {
        return GameInstance(GameConfig(gameDifficulty, gameFormat, gameMode, gameParameter),
                            playlist)
    }
}

data class GameConfig(
    val difficulty: GameDifficulty,
    val format: GameFormat,
    val mode: GameMode,
    val parameter: GameParameter
)

data class GameParameter(
    val round: Int,
    val funny: Boolean,
)

enum class GameDifficulty(val timeToFind: Int, val hint: Boolean) {
    EASY(46000, true),
    MEDIUM(31000, true),
    DIFFICULT(1000, true)
    //SHAZAM(15, false)
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