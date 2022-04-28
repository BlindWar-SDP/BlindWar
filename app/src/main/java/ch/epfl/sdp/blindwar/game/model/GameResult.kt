package ch.epfl.sdp.blindwar.game.model

import ch.epfl.sdp.blindwar.game.model.config.GameMode

class GameResult(var gameMode: GameMode = GameMode.REGULAR,
                var gameNbrRound: Int = 0,
                var gameScore: Int = 0)
