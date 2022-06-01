package ch.epfl.sdp.blindwar.game.model

import ch.epfl.sdp.blindwar.game.model.config.GameFormat
import ch.epfl.sdp.blindwar.game.model.config.GameMode
import ch.epfl.sdp.blindwar.profile.model.Result

/**
 * Results for the game over
 *
 * @property gameMode
 * @property mode
 * @property result
 * @property gameNbrRound
 * @property gameScore
 * @property gameTime
 */
class GameResult(
    var gameMode: GameMode = GameMode.REGULAR,
    var mode: GameFormat = GameFormat.SOLO,
    var result: Result = Result.DRAW,
    var gameNbrRound: Int = 0,
    var gameScore: Int = 0,
    var gameTime: String = "never"
)
