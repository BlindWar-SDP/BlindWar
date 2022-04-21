package ch.epfl.sdp.blindwar.game.model

import android.content.Context
import ch.epfl.sdp.blindwar.database.UserDatabase
import com.google.firebase.auth.FirebaseAuth
import ch.epfl.sdp.blindwar.data.music.MusicMetadata
import ch.epfl.sdp.blindwar.game.util.GameHelper
import ch.epfl.sdp.blindwar.audio.MusicViewModel

/**
 * Class representing an instance of a game
 *
 * @param gameInstance object that defines the parameters / configuration of a game
 * @param context of the Game
 * @constructor Construct a class that represent the game logic
 */
abstract class Game(
    gameInstance: GameInstance,
    protected val context: Context
) {
    /** Encapsulates the characteristics of a game instead of its logic
     *
     */
    protected val game: GameInstance = gameInstance

    protected lateinit var musicViewModel: MusicViewModel

    private val gameParameter: GameParameter = gameInstance
        .gameConfig
        .parameter

    /** Player game score **/
    var score = 0
        protected set

    var round = 0
        protected set

    /**
     * Prepares the game following the configuration
     *
     */
    abstract fun init()

    /**
     * Record the game instance to the player history
     * clean up player and assets
     *
     */
    private fun endGame() {
        val fails = round - score
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            UserDatabase.updateSoloUserStatistics(currentUser.uid, score, fails)
        }

        musicViewModel.soundTeardown()
    }

    /**
     * Pass to the next round
     *
     * @return true if the game is over after this round, false otherwise
     */
    fun nextRound(): Boolean {
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
        if (gameParameter.hint) {
            return musicViewModel.getCurrentMetadata()
        }

        return null
    }

    /**
     * Try to guess a music by its title
     *
     * @param titleGuess Title that the user guesses
     * @return True if the guess is correct
     */
    fun guess(titleGuess: String, isVocal: Boolean): Boolean {
        return if (
            GameHelper.isTheCorrectTitle(titleGuess, currentMetadata()!!.title, isVocal)
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
}