package ch.epfl.sdp.blindwar.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.blindwar.domain.game.GameConfig
import ch.epfl.sdp.blindwar.domain.game.GameInstance
import ch.epfl.sdp.blindwar.domain.game.GameMode
import ch.epfl.sdp.blindwar.domain.game.GameParameter

class GameInstanceViewModel: ViewModel() {
    private lateinit var gameInstance: MutableLiveData<GameInstance>

    fun setGameConfig(gameConfig: GameConfig) {
        // Use GameConfig builder
    }

    fun setGameMode(gameMode: GameMode) {
        // Use GameConfig builder
    }

    fun setGameParameters(gameParameter: GameParameter) {
        // Use GameConfig builder
    }
}