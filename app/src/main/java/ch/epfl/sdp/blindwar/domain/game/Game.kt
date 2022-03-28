package ch.epfl.sdp.blindwar.domain.game

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
    abstract fun endGame()

    /**
     * Pass to the next round
     *
     * @return true if the game is over after this round, false otherwise
     */
    abstract fun nextRound(): Boolean

    /**
     * Try to guess a music by its title
     *
     * @param titleGuess Title that the user guesses
     * @return True if the guess is correct
     */
    abstract fun guess(titleGuess: String): Boolean

    /**
     * Current round has timed out
     *
     */
    abstract fun timeout(): Unit

    /**
     * Play the current music if in pause
     *
     */
    abstract fun play()

    /**
     * Pause the current music if playing
     *
     */
    abstract fun pause()
}