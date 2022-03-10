package ch.epfl.sdp.blindwar.game

abstract class Game {
    protected var score = 0;
    protected lateinit var title: String;

    /**
     * Pass to the next round
     *
     */
    abstract fun nextRound();

    /**
     * Try to guess a music by its title
     *
     * @param titleGuess Title that the user guesses
     */
    abstract fun guess(titleGuess: String);
}