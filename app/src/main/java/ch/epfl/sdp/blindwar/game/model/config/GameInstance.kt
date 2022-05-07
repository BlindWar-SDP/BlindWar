package ch.epfl.sdp.blindwar.game.model.config

import ch.epfl.sdp.blindwar.game.model.Playlist
import ch.epfl.sdp.blindwar.game.util.GameUtil

data class GameInstance(
    val gameConfig: GameConfig, // configuration object of the game
    val onlinePlaylist: Playlist, // playlist of the game
    val gameFormat: GameFormat
) {
    class Builder {
        private var playlist: Playlist = GameUtil.gameInstanceSolo
            .onlinePlaylist

        private var gameFormat: GameFormat = GameFormat.SOLO

        private var gameParameter: GameParameter = GameUtil.gameInstanceSolo
            .gameConfig
            .parameter

        private var gameMode: GameMode = GameUtil.gameInstanceSolo
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
            setPlaylist(gameInstance.onlinePlaylist)
            setFormat(gameInstance.gameFormat)
            setParameter(gameInstance.gameConfig.parameter)
            setMode(gameInstance.gameConfig.mode)
            return this
        }

        fun build(): GameInstance {
            return GameInstance(
                GameConfig(gameMode, gameParameter),
                playlist,
                gameFormat
            )
        }
    }
}

