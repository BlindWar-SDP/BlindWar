package ch.epfl.sdp.blindwar.game.model

import ch.epfl.sdp.blindwar.game.model.config.GameMode
import kotlinx.serialization.Serializable

@Serializable
class GameResult(var gameMode: GameMode = GameMode.REGULAR,
                var gameNbrRound: Int = 0,
                var gameScore: Int = 0)
