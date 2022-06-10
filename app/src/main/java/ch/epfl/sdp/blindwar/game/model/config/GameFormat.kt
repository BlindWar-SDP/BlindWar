package ch.epfl.sdp.blindwar.game.model.config

import java.io.Serializable

/**
 * Format = SOLO or MULTI
 */
enum class GameFormat: Serializable {
    SOLO,
    MULTI
}