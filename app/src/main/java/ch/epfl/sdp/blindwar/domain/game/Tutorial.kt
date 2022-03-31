package ch.epfl.sdp.blindwar.domain.game

import ch.epfl.sdp.blindwar.domain.game.SongImageUrlConstants.META_DATA_TUTORIAL_MUSICS_PER_AUTHOR

object Tutorial {

    private val playlist: List<SongMetaData> = META_DATA_TUTORIAL_MUSICS_PER_AUTHOR.values.toList()

    private val gameParameter = GameParameter(3, funny = false, hint = true, 31000)

    private val gameConfig = GameConfig(
        GameDifficulty.EASY,
        GameFormat.SOLO, GameMode.REGULAR, gameParameter
    )

    val gameInstance = GameInstance(gameConfig, playlist)
}