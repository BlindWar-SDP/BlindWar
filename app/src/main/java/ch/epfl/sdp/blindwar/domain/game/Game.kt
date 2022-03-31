package ch.epfl.sdp.blindwar.domain.game

import android.content.res.AssetManager
import java.util.*

/**
 * Class representing an instance of a game
 *
 * @constructor
 * Construct a class that represent the game logic
 *
 */
abstract class Game(gameInstance: GameInstance, assetManager: AssetManager) {
    /** Encapsulates the characteristics of a game instead of its logic **/
    private val game: GameInstance = gameInstance

    private val gameParameter: GameParameter = gameInstance
        .gameConfig
        .parameter

    /** Get the sound data through another layer **/
    protected val gameSound = GameSound(assetManager)

    /** Player game score **/
    var score = 0
        protected set

    private var round = 0

    /**
     * Prepares the game following the configuration
     *
     */
    abstract fun init()

    /**
     * Record the game instance to the player history
     * clean up player and assets
     */
    private fun endGame() {
        gameSound.soundTeardown()
    }

    /**
     * Pass to the next round
     *
     * @return true if the game is over after this round, false otherwise
     */
    fun nextRound(fromLocalStorage: Boolean = false): Boolean {
        if (round >= gameParameter.round) {
            endGame()
            return true
        }

        gameSound.nextRound(fromLocalStorage)
        return false
    }

    /**
     * Depends on the game instance parameter
     */
    fun currentMetadata(): SongMetaData? {
        if (gameParameter.hint) {
            return gameSound.getCurrentMetadata()
        }

        return null
    }

    /**
     * Try to guess a music by its title
     *
     * @param titleGuess Title that the user guesses
     * @return True if the guess is correct
     */
    fun guess(titleGuess: String): Boolean {
        return if (titleGuess.uppercase(Locale.getDefault()) == currentMetadata()?.title?.uppercase(
                Locale.getDefault()
            )
        ) {
            score += 1
            round += 1
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
        gameSound.play()
    }

    /**
     * Pause the current music if playing
     *
     */
    fun pause() {
        gameSound.pause()
    }
}