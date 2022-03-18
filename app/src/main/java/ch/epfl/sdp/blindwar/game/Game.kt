package ch.epfl.sdp.blindwar.game

/**
 * Class representing an instance of a game
 *
 * @constructor
 * Construct a class that represent the game logic
 *
 * @param timeToFind Time to find the music
 */
abstract class Game(protected val timeToFind: Int) {
    var score = 0
        protected set
    protected var title: String? = null

    /**
     * Pass to the next round
     *
     * @return The meta data representing the new current music or null if there was an error
     */
    abstract fun nextRound(): MusicMetaData?

    /**
     * Try to guess a music by its title
     *
     * @param titleGuess Title that the user guesses
     * @return True if the guess is correct
     */
    abstract fun guess(titleGuess: String): Boolean

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