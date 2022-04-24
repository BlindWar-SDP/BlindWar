package ch.epfl.sdp.blindwar.game.model.config

data class GameParameter(
    val round: Int, // number of rounds
    val timeToFind: Int, // timer during round (timed mode)
    val funny: Boolean, // indicates if the music is altered
    val hint: Boolean, // indicates if song author is given
    val lives: Int // number of lives (survival mode)
)