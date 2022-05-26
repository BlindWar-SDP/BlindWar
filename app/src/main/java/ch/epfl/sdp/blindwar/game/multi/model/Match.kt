package ch.epfl.sdp.blindwar.game.multi.model

import ch.epfl.sdp.blindwar.game.model.config.GameInstance


/**
 * Match class
 *
 * @property listPlayers
 * @property game instance where is stored the playlist, game mode, game formats etc
 * @property listResult (same index as user number of music guessed)
 * @property maxPlayer
 */
data class Match(
    var uid: String = "",
    var elo: Int = 0,
    var listPlayers: MutableList<String>? = null,
    var listPseudo: MutableList<String>? = null,
    val game: GameInstance? = null,
    var listResult: MutableList<Int>? = null,
    var listFinished: MutableList<Boolean>? = null,
    var maxPlayer: Int = 2,
    var isPrivate: Boolean = false,
    var isStarted: Boolean = false
)