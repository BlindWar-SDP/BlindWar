package ch.epfl.sdp.blindwar.database

import ch.epfl.sdp.blindwar.profile.model.Mode
import ch.epfl.sdp.blindwar.profile.model.User


/**
 * Match class
 *
 * @property listPlayers
 * @property mode (TODO check funny modes and if playlist must be saved)
 * @property listResult (same index as user number of music guessed)
 * @property numberOfMusics (total number to calculate scores)
 */
data class Match(
    var uid: String = "",
    var listPlayers: MutableList<User>? = null,
    val mode: Mode = Mode.MULTI, //TODO check how to save funny modes
    var listResult: MutableList<Int>? = null,
    var numberOfMusics: Int = 0,
    var maxPlayer: Int = 2
)