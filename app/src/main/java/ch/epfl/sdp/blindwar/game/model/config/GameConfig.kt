package ch.epfl.sdp.blindwar.game.model.config

data class GameConfig(
    val format: GameFormat,
    val mode: GameMode,
    val parameter: GameParameter
)