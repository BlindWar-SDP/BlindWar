package ch.epfl.sdp.blindwar.domain.game

import ch.epfl.sdp.blindwar.domain.game.GameInstance
import ch.epfl.sdp.blindwar.domain.game.SongImageUrlConstants.SONG_MAP

object Tutorial {

    private val playlist: List<SongMetaData> = SONG_MAP.values.toList()

    private val gameParameter = GameParameter(3, funny = false, hint = true, 30000)
    private val gameParameterTest = GameParameter(1, funny = false, hint = true, timeToFind = 1000)

    private val gameConfig = GameConfig(GameDifficulty.EASY,
        GameFormat.SOLO, GameMode.REGULAR, gameParameter)

    private val gameConfigTest = GameConfig(GameDifficulty.EASY,
        GameFormat.SOLO, GameMode.REGULAR, gameParameterTest)

    val gameInstance = GameInstance(gameConfig, playlist)
    val gameInstanceTest = GameInstance(gameConfig, playlist)
}