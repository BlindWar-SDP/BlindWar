package ch.epfl.sdp.blindwar.game.model

data class GameConfig(
    val format: GameFormat,
    val mode: GameMode,
    val parameter: GameParameter
)