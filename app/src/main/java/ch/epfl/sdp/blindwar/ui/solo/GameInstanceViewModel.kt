package ch.epfl.sdp.blindwar.ui.solo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    /** TODO: Complete for next sprint
    fun setGameDifficulty(gameDifficulty: GameDifficulty) {
        gameInstance.value = GameInstanceBuilder().setGameInstance(gameInstance.value!!)
            .setDifficulty(gameDifficulty)
            .build()
    }

    fun setGameParameters(gameParameter: GameParameter) {
        gameInstance.value = GameInstanceBuilder().setGameInstance(gameInstance.value!!)
            .setParameter(gameParameter)
            .build()
    }

    fun setGamePlaylist(playlist: List<SongMetaData>) {
        gameInstance.value = GameInstanceBuilder().setGameInstance(gameInstance.value!!)
            .setPlaylist(playlist)
            .build()
    }
    **/
}