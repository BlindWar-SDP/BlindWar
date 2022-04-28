package ch.epfl.sdp.blindwar.game.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.blindwar.game.model.*
import ch.epfl.sdp.blindwar.game.model.config.GameInstance
import ch.epfl.sdp.blindwar.game.model.config.GameMode
import ch.epfl.sdp.blindwar.game.model.config.GameParameter
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

    private val currentParameter = gameInstance
        .value
        ?.gameConfig
        ?.parameter

    private val mode = gameInstance
        .value!!
        .gameConfig
        .mode

    /**
     * Setter for the game mode
     *
     * @param gameMode
     */
    fun setGameMode(gameMode: GameMode) {
        gameInstance.value = GameInstance.Builder().setGameInstance(gameInstance.value!!)
            .setMode(gameMode)
            .build()
    }

    /**
     * Setter for the sound alteration parameter
     *
     * @param funny
     */
    fun setGameFunny(funny: Boolean) {
        gameInstance.value = GameInstance.Builder().setGameInstance(gameInstance.value!!)
            .setParameter(
                GameParameter(round = currentParameter?.round!!,
                timeToFind = currentParameter.timeToFind,
                hint = currentParameter.hint,
                funny = currentParameter.funny,
                    lives = currentParameter.lives
            )
            )
            .build()
    }

    /**
     * Setter for the game playlist
     *
     * @param timeChosen
     * @param roundChosen
     * @param playlist
     */
    fun setGameParameters(timeChosen: Int, roundChosen: Int, playlist: Playlist) {
        gameInstance.value = GameInstance.Builder().setGameInstance(gameInstance.value!!)
            .setPlaylist(playlist)
            .setParameter(
                GameParameter(
                    round = if (mode == GameMode.SURVIVAL) playlist.songs.size else roundChosen,
                    timeToFind = timeChosen,
                    hint = currentParameter?.hint!!,
                    funny = currentParameter.funny,
                    lives = if (mode == GameMode.SURVIVAL) roundChosen else currentParameter.lives
                )
            )
            .build()
    }
}