package ch.epfl.sdp.blindwar.game.model.config

/**
 * Class used to store game mode and game parameter
 *
 * @property mode
 * @property parameter
 */
data class GameConfig(
    val mode: GameMode? = null,
    val parameter: GameParameter? = null
)