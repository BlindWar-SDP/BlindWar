package ch.epfl.sdp.blindwar.domain.game

import ch.epfl.sdp.blindwar.domain.game.GameInstance
import ch.epfl.sdp.blindwar.domain.game.SongImageUrlConstants.SONG_MAP

object Tutorial {

    private val playlist: List<SongMetaData> = arrayListOf(SongMetaData("Mistral gagnant", "Renaud", SONG_MAP["Renaud"]!!),
    SongMetaData("Poker Face", "Lady Gaga", SONG_MAP["Lady Gaga"]!!)
    )

    private val gameParameter = GameParameter(10, funny = false, hint = true, 5000)

    private val gameConfig: GameConfig = GameConfig(GameDifficulty.EASY,
        GameFormat.SOLO, GameMode.REGULAR, gameParameter)

    val gameInstance: GameInstance = GameInstance(gameConfig, playlist, arrayListOf())
}