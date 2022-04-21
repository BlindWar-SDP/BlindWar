package ch.epfl.sdp.blindwar.game.model

data class GameParameter(
    val round: Int,
    val timeToFind: Int,
    val funny: Boolean,
    val hint: Boolean
)