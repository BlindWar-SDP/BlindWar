package ch.epfl.sdp.blindwar.domain.game

import ch.epfl.sdp.blindwar.domain.game.GameInstance
import ch.epfl.sdp.blindwar.domain.game.SongImageUrlConstants.SONG_MAP

object Tutorial {

    private val playlist: List<SongMetaData> = SONG_MAP.values.toList()

    private val gameParameter = GameParameter(3, funny = false)
    private val gameParameterTest = GameParameter(1, funny = false)

    private val gameConfig = GameConfig(GameDifficulty.DIFFICULT,
        GameFormat.SOLO, GameMode.REGULAR, gameParameter)

    val gameInstance = GameInstance(gameConfig, playlist)
}