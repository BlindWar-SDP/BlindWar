package ch.epfl.sdp.blindwar.game.model

import ch.epfl.sdp.blindwar.game.util.Tutorial

data class GameInstance(
    val gameConfig: GameConfig,
    val playlist: Playlist
) {
    class Builder() {
        private var playlist: Playlist = Tutorial.gameInstance
            .playlist

        private var gameFormat: GameFormat = GameFormat.SOLO

        private var gameParameter: GameParameter = Tutorial.gameInstance
            .gameConfig
            .parameter

        private var gameMode: GameMode = Tutorial.gameInstance
            .gameConfig
            .mode

        fun setPlaylist(playlist: Playlist): Builder {
            this.playlist = playlist
            return this
        }

        fun setFormat(gameFormat: GameFormat): Builder {
            this.gameFormat = gameFormat
            return this
        }

        fun setParameter(gameParameter: GameParameter): Builder {
            this.gameParameter = gameParameter
            return this
        }

        fun setMode(gameMode: GameMode): Builder {
            this.gameMode = gameMode
            return this
        }

        fun setGameInstance(gameInstance: GameInstance): Builder {
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
}

