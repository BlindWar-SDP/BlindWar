package ch.epfl.sdp.blindwar.game.model

import ch.epfl.sdp.blindwar.game.model.config.GameMode
import ch.epfl.sdp.blindwar.profile.model.Mode
import ch.epfl.sdp.blindwar.profile.model.Result
import kotlinx.serialization.Serializable

class GameResult(
    var gameMode: GameMode = GameMode.REGULAR,
    var mode: Mode = Mode.SOLO,
    var result: Result = Result.DRAW,
    var gameNbrRound: Int = 0,
    var gameScore: Int = 0
)
