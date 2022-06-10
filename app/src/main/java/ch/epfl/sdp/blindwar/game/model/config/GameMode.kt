package ch.epfl.sdp.blindwar.game.model.config

import java.io.Serializable

/**
 * Game modes available
 */
enum class GameMode: Serializable {
    REGULAR,
    SURVIVAL,
    TIMED
}