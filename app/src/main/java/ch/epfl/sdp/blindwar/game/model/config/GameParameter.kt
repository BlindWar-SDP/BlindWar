package ch.epfl.sdp.blindwar.game.model.config

data class GameParameter(
    val round: Int = 0, // number of rounds
    val timeToFind: Int = 0, // timer during round (timed mode)
    val funny: Boolean = false, // indicates if the music is altered
    val hint: Boolean = false, // indicates if song author is given
    val lives: Int = 0// number of lives (survival mode)
)