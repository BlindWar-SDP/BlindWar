package ch.epfl.sdp.blindwar.game.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.blindwar.game.model.GameInstance
import ch.epfl.sdp.blindwar.game.model.GameMode
import ch.epfl.sdp.blindwar.game.model.GameParameter
import ch.epfl.sdp.blindwar.game.model.Playlist
import ch.epfl.sdp.blindwar.game.util.Tutorial

/**
 * Game Instance viewModel used during game creation
 *
 * @constructor creates a GameInstanceViewModel
 */
class GameInstanceViewModel: ViewModel() {
    var gameInstance = MutableLiveData<GameInstance>().let {
        it.value = Tutorial.gameInstance
        it
    }

    /**
     * Setter for the game mode
     */
    fun setGameMode(gameMode: GameMode) {
        gameInstance.value = GameInstance.Builder().setGameInstance(gameInstance.value!!)
            .setMode(gameMode)
            .build()
    }

    /**
     * Setter for the sound alteration parameter
     */
    fun setGameFunny(funny: Boolean) {
        val currentParameter = gameInstance
            .value
            ?.gameConfig
            ?.parameter

        gameInstance.value = GameInstance.Builder().setGameInstance(gameInstance.value!!)
            .setParameter(
                GameParameter(round = currentParameter?.round!!,
                timeToFind = currentParameter.timeToFind,
                hint = currentParameter.hint,
                funny = currentParameter.funny
            )
            )
            .build()
    }

    /**
     * Setter for the countdown time of the game
     */
    fun setGameTimeRound(timeChosen: Int, roundChosen: Int) {
        val currentParameter = gameInstance
            .value
            ?.gameConfig
            ?.parameter

        gameInstance.value = GameInstance.Builder().setGameInstance(gameInstance.value!!)
            .setParameter(GameParameter(round = roundChosen,
                timeToFind = timeChosen,
                hint = currentParameter?.hint!!,
                funny = currentParameter.funny
            ))
            .build()
    }

    /**
     * Setter for the game playlist
     */
    fun setGamePlaylist(playlist: Playlist) {
        gameInstance.value = GameInstance.Builder().setGameInstance(gameInstance.value!!)
            .setPlaylist(playlist)
            .build()
    }
}