package ch.epfl.sdp.blindwar.game

abstract class Game {
    var score = 0
        protected  set
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
     * @return The new score
     */
    abstract fun guess(titleGuess: String): Int
}