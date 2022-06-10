package ch.epfl.sdp.blindwar.game.model.config

import java.io.Serializable


/**
 * Parameters for the games
 *
 * @property round number of rounds
 * @property timeToFind timer during round (timed mode)
 * @property funny indicates if the music is altered
 * @property hint indicates if song author is given
 * @property lives number of lives (survival mode)
 */
data class GameParameter(
    val round: Int = 0,
    val timeToFind: Int = 0,
    val funny: Boolean = false,
    val hint: Boolean = false,
    val lives: Int = 0
) : Serializable