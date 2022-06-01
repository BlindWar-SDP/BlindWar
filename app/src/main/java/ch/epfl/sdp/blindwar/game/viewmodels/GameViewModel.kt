@file:Suppress("ControlFlowWithEmptyBody", "ControlFlowWithEmptyBody")

package ch.epfl.sdp.blindwar.game.viewmodels

import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.blindwar.audio.MusicViewModel
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata
import ch.epfl.sdp.blindwar.game.model.GameResult
import ch.epfl.sdp.blindwar.game.model.config.GameFormat
import ch.epfl.sdp.blindwar.game.model.config.GameInstance
import ch.epfl.sdp.blindwar.game.model.config.GameMode
import ch.epfl.sdp.blindwar.game.model.config.GameParameter
import ch.epfl.sdp.blindwar.game.util.GameHelper
import ch.epfl.sdp.blindwar.game.util.ScoreboardAdapter
import ch.epfl.sdp.blindwar.profile.model.Result
import ch.epfl.sdp.blindwar.profile.viewmodel.ProfileViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Class representing an instance of a game
 *
 * @param gameInstance object that defines the parameters / configuration of a game
 * @constructor Construct a class that represent the game logic
 */
open class GameViewModel(
    /** Encapsulates the characteristics of a game instead of its logic
     *
     */
    protected val gameInstance: GameInstance,
    private val resources: Resources,
    private var scoreboardAdapter: ScoreboardAdapter? = null
) : ViewModel() {
    private lateinit var musicViewModel: MusicViewModel
    protected val profileViewModel = ProfileViewModel()

    private val gameParameter: GameParameter = gameInstance
        .gameConfig!!
        .parameter!!

    protected val mode: GameMode = gameInstance
        .gameConfig!!
        .mode!!

    /** Player game score **/
    var score = 0
        protected set

    var round = 0
        protected set

    /** Survival mode specific **/
    val lives = MutableLiveData(gameParameter.lives)

    /**
     * Prepares the game following the configuration
     *
     */
    fun createMusicViewModel(context: Context) {
        musicViewModel = MusicViewModel(
            gameInstance.onlinePlaylist!!,
            context, resources
        )
    }

    /**
     * Play the current music if in pause
     *
     */
    fun play() {
        musicViewModel.play()
    }

    /**
     * Pause the current music if playing
     *
     */
    fun pause() {
        musicViewModel.pause()
    }

    /**
     * Increment the number of point of a player in the scoreboard
     *
     * @param playerName Name of the player
     * @return
     */
    fun incrementPoint(playerName: String) = scoreboardAdapter?.incrementPoint(playerName)

    /**
     * Record the game instance to the player history
     * clean up player and assets
     *
     */
    private fun endGame() {
        if (gameInstance.gameFormat == GameFormat.SOLO) {
            val fails = round - score
            val result = if (fails == 0) Result.WIN else Result.LOSS

            var formatted = "never"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                formatted = current.format(formatter)
            }
            val gameResult = GameResult(mode, GameFormat.SOLO, result, round, score, formatted)

            profileViewModel.updateStats(score, fails, gameResult)
            musicViewModel.soundTeardown()
        } else {
            //TODO GAME OVER
        }
    }

    /**
     * Pass to the next round
     *
     * @return true if the game is over after this round, false otherwise
     */
    fun nextRound(): Boolean {
        if (mode == GameMode.SURVIVAL && lives.value!! <= 0) {
            endGame()
            return true
        }

        if (round >= gameParameter.round) {
            endGame()
            return true
        }

        musicViewModel.nextRound()
        musicViewModel.normalMode()
        return false
    }

    /**
     * Depends on the game instance parameter
     *
     * @return
     */
    fun currentMetadata(): MusicMetadata? {
        return if (gameParameter.hint) {
            musicViewModel.getCurrentMetadata()
        } else null
    }

    /**
     * Try to guess a music by its title
     *
     * @param titleGuess Title that the user guesses
     * @return True if the guess is correct
     */
    fun guess(titleGuess: String, isVocal: Boolean): Boolean {
        return if (
            GameHelper.isTheCorrectTitle(titleGuess, currentMetadata()!!.name, isVocal)
        ) {
            score += 1
            round += 1
            musicViewModel.summaryMode()
            true
        } else
            false
    }

    /**
     * Current round has timed out
     *
     */
    fun timeout() {
        round += 1
        lives.value = lives.value?.minus(1)
    }
}