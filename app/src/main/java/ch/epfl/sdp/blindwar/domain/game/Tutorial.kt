package ch.epfl.sdp.blindwar.domain.game

import ch.epfl.sdp.blindwar.domain.game.GameInstance
import ch.epfl.sdp.blindwar.domain.game.SongImageUrlConstants.SONG_MAP

object Tutorial {

    private val playlist: List<SongMetaData> = SONG_MAP.values.toList()

    private val gameParameter = GameParameter(2, funny = false, hint = true, 20000)

    private val gameConfig: GameConfig = GameConfig(GameDifficulty.EASY,
        GameFormat.SOLO, GameMode.REGULAR, gameParameter)

    val gameInstance: GameInstance = GameInstance(gameConfig, playlist, arrayListOf())
}