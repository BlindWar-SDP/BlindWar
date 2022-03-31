package ch.epfl.sdp.blindwar.domain.game

import java.util.*

/**
 * Class representing an instance of a game
 *
 * @constructor
 * Construct a class that represent the game logic
 *
 */
abstract class Game(gameInstance: GameInstance) {
    /** Encapsulates the characteristics of a game instead of its logic **/
    private val game: GameInstance = gameInstance

    protected val gameDifficulty: GameDifficulty = gameInstance
        .gameConfig
        .difficulty

    protected val gameParameter: GameParameter = gameInstance
        .gameConfig
        .parameter

    /** TODO: implement other game format and modes
    protected val gameDifficulty: GameDifficulty = gameInstance
        .gameConfig
        .difficulty

    protected val gameFormat: GameFormat = gameInstance
        .gameConfig
        .format
    **/

    private val gamePlaylist: List<SongMetaData> = gameInstance.playlist

    /** Get the sound data through another layer **/
    abstract val gameSoundController: GameSoundController

    /** Player game score **/
    var score = 0
        protected  set

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
     */
    fun endGame() {
        gameSoundController.soundTeardown()
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

        gameSoundController.nextRound(fromLocalStorage)
        return false
    }

    /**
     * Depends on the game instance parameter
     */
    fun currentMetadata(): SongMetaData? {
        if (gameDifficulty.hint) {
            return gameSoundController.getCurrentMetadata()
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
                Locale.getDefault())) {
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
        gameSoundController.play()
    }

    /**
     * Pause the current music if playing
     *
     */
    fun pause() {
        gameSoundController.pause()
    }
}