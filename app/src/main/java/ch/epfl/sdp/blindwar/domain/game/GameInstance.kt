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
        setFormat(gameInstance.gameConfig.format)
        setParameter(gameInstance.gameConfig.parameter)
        setMode(gameInstance.gameConfig.mode)
        return this
    }

    fun build(): GameInstance {
        return GameInstance(GameConfig(gameFormat, gameMode, gameParameter),
                            playlist)
    }
}

data class GameConfig(
    val format: GameFormat,
    val mode: GameMode,
    val parameter: GameParameter
)

data class GameParameter(
    val round: Int,
    val timeToFind: Int,
    val funny: Boolean,
    val hint: Boolean
)

enum class GameFormat {
    SOLO,
    TEAM
}

enum class GameMode {
    REGULAR,
    SURVIVAL,
    TIMED
}