package ch.epfl.sdp.blindwar.game.viewmodels
/*
import android.content.Context
import android.content.res.Resources
import ch.epfl.sdp.blindwar.game.model.config.GameInstance
import ch.epfl.sdp.blindwar.game.util.ScoreboardAdapter

/**
 * Class representing an instance of a game
 *
 * @param gameInstance object that defines the parameters / configuration of a game
 * @param context of the Game
 * @constructor Construct a class that represent the game logic
 */
class GameViewModelMulti(
    gameInstance: GameInstance,
    context: Context,
    resources: Resources,
    private var scoreboardData: MutableList<Pair<Int, String>>,
    private var scoreboardAdapter: ScoreboardAdapter
) : GameViewModel(gameInstance, context, resources) {
    fun incrementPoint(playerName: String) = scoreboardData.indexOfFirst { it.second == playerName }.let { scoreboardData.set(it, Pair(scoreboardData[it].first + 1, playerName))}
}

 */