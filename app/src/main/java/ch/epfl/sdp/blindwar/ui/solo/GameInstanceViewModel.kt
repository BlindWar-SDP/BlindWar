package ch.epfl.sdp.blindwar.ui.solo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.blindwar.data.music.MusicMetadata
import ch.epfl.sdp.blindwar.domain.game.*

class GameInstanceViewModel: ViewModel() {
    var gameInstance = MutableLiveData<GameInstance>().let {
        it.value = Tutorial.gameInstance
        it
    }

    fun setGameMode(gameMode: GameMode) {
        gameInstance.value = GameInstanceBuilder().setGameInstance(gameInstance.value!!)
            .setMode(gameMode)
            .build()
    }

    fun setGameFunny(funny: Boolean) {
        val currentParameter = gameInstance
            .value
            ?.gameConfig
            ?.parameter

        gameInstance.value = GameInstanceBuilder().setGameInstance(gameInstance.value!!)
            .setParameter(GameParameter(round = currentParameter?.round!!,
                timeToFind = currentParameter.timeToFind,
                hint = currentParameter.hint,
                funny = currentParameter.funny
            ))
            .build()
    }

    fun setGameTimeRound(timeChosen: Int, roundChosen: Int) {
        val currentParameter = gameInstance
            .value
            ?.gameConfig
            ?.parameter

        gameInstance.value = GameInstanceBuilder().setGameInstance(gameInstance.value!!)
            .setParameter(GameParameter(round = roundChosen,
                timeToFind = timeChosen,
                hint = currentParameter?.hint!!,
                funny = currentParameter.funny
            ))
            .build()
    }

    fun setGamePlaylist(playlist: PlaylistModel) {
        gameInstance.value = GameInstanceBuilder().setGameInstance(gameInstance.value!!)
            .setPlaylist(playlist)
            .build()
    }
}