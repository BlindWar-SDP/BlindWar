package ch.epfl.sdp.blindwar.game.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.blindwar.database.MatchDatabase
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.game.model.Playlist
import ch.epfl.sdp.blindwar.game.model.config.GameFormat
import ch.epfl.sdp.blindwar.game.model.config.GameInstance
import ch.epfl.sdp.blindwar.game.model.config.GameMode
import ch.epfl.sdp.blindwar.game.model.config.GameParameter
import ch.epfl.sdp.blindwar.game.multi.model.Match
import ch.epfl.sdp.blindwar.game.util.GameUtil
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * Game Instance viewModel used during game creation
 *
 * @constructor creates a GameInstanceViewModel
 */
class GameInstanceViewModel : ViewModel() {
    var gameInstance = MutableLiveData<GameInstance>().let {
        it.value = GameUtil.gameInstanceSolo
        it
    }
    var match: Match? = null
    private var isPrivate = false
    private var maxPlayer = 2

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
        val currentParameter = gameInstance
            .value
            ?.gameConfig
            ?.parameter

        gameInstance.value = GameInstance.Builder().setGameInstance(gameInstance.value!!)
            .setParameter(
                GameParameter(
                    round = currentParameter?.round!!,
                    timeToFind = currentParameter.timeToFind,
                    hint = currentParameter.hint,
                    funny = funny,
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
        val mode = gameInstance
            .value!!
            .gameConfig
            .mode

        val currentParameter = gameInstance
            .value
            ?.gameConfig
            ?.parameter

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

    fun setGameFormat(gameFormat: GameFormat) {
        gameInstance.value = GameInstance.Builder().setGameInstance(gameInstance.value!!)
            .setFormat(gameFormat)
            .build()
    }

    /**
     * Set parameters for multiplayer mode
     *
     * @param isPrivate
     * @param maxPlayer
     */
    fun setMultiParameters(isPrivate: Boolean, maxPlayer: Int) {
        this.isPrivate = isPrivate
        this.maxPlayer = maxPlayer
    }

    /**
     * create match for multiplayer mode
     *
     */
    fun createMatch(): Match {
        UserDatabase.getCurrentUser().let {
            val user =
                it!!.getValue(User::class.java) as User //TODO find better solution to avoid active waiting
            match = MatchDatabase.createMatch(
                user.uid,
                user.pseudo,
                user.userStatistics.elo,
                maxPlayer,
                gameInstance.value!!,
                Firebase.firestore,
                isPrivate
            )
            return match!!
        }
    }
}