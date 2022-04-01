package ch.epfl.sdp.blindwar.domain.game

import ch.epfl.sdp.blindwar.domain.game.SongImageUrlConstants.META_DATA_TUTORIAL_MUSICS_PER_AUTHOR

object Tutorial {

    private val playlist: List<SongMetaData> = META_DATA_TUTORIAL_MUSICS_PER_AUTHOR.values.toList()

    private val gameParameter = GameParameter(3, funny = false)
    private val gameParameterTest = GameParameter(1, funny = false)

    private val gameConfig = GameConfig(GameDifficulty.DIFFICULT,
        GameFormat.SOLO, GameMode.REGULAR, gameParameter)

    val gameInstance = GameInstance(gameConfig, playlist)
}