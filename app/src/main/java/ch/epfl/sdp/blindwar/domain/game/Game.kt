package ch.epfl.sdp.blindwar.domain.game

import android.content.Context
import android.content.res.AssetManager
import ch.epfl.sdp.blindwar.data.music.MusicMetadata

/**
 * Class representing an instance of a game
 *
 * @constructor
 * Construct a class that represent the game logic
 *
 */
abstract class Game(
    gameInstance: GameInstance,
    protected val context: Context
) {
    /** Encapsulates the characteristics of a game instead of its logic **/
    protected val game: GameInstance = gameInstance

    protected lateinit var musicController: MusicController

    private val gameParameter: GameParameter = gameInstance
        .gameConfig
        .parameter

    /** Player game score **/
    var score = 0
        protected set

    var round = 0

    /**
     * Prepares the game following the configuration
     *
     */
    abstract fun init()

    /**
     * Record the game instance to the player history
     * clean up player and assets
     */
    fun endGame() {
        musicController.soundTeardown()
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

        musicController.nextRound()
        musicController.normalMode()
        return false
    }

    /**
     * Depends on the game instance parameter
     */
    fun currentMetadata(): MusicMetadata? {
        if (gameParameter.hint) {
            return musicController.getCurrentMetadata()
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
            musicController.summaryMode()
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
        musicController.play()
    }

    /**
     * Pause the current music if playing
     *
     */
    fun pause() {
        musicController.pause()
    }
}