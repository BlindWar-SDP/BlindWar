package ch.epfl.sdp.blindwar.user

import kotlinx.serialization.Serializable
import kotlin.math.pow
import kotlin.math.round

@Serializable
class AppStatistics {
    private val numberOfMode = Mode.values().size
    private val hundredPercent = 100
    private val standardEqualValue = 8

    var correctArray = IntArray(numberOfMode).toMutableList()
        private set
    var wrongArray = IntArray(numberOfMode).toMutableList()
        private set
    var correctPercent = FloatArray(numberOfMode).toMutableList()
        private set
    var wrongPercent = FloatArray(numberOfMode).toMutableList()
        private set
    var wins = IntArray(numberOfMode).toMutableList()
        private set
    var losses = IntArray(numberOfMode).toMutableList()
        private set
    var draws = IntArray(numberOfMode).toMutableList()
        private set
    var winPercent = FloatArray(numberOfMode).toMutableList()
        private set
    var lossPercent = FloatArray(numberOfMode).toMutableList()
        private set
    var drawPercent = FloatArray(numberOfMode).toMutableList()
        private set
    var elo: Int = 1000
        private set

    /**
     * Sets ELO
     *
     * @param newElo the new ELO to set
     */
    fun eloSetter(newElo: Int) {
        elo = newElo
    }

    /**
     * Resets all the statistics except for the ELO
     *
     */
    fun resetStatistics() {
        correctArray = IntArray(numberOfMode).toMutableList()
        wrongArray = IntArray(numberOfMode).toMutableList()
        correctPercent = FloatArray(numberOfMode).toMutableList()
        wrongPercent = FloatArray(numberOfMode).toMutableList()
        wins = IntArray(numberOfMode).toMutableList()
        losses = IntArray(numberOfMode).toMutableList()
        draws = IntArray(numberOfMode).toMutableList()
        winPercent = FloatArray(numberOfMode).toMutableList()
        lossPercent = FloatArray(numberOfMode).toMutableList()
        drawPercent = FloatArray(numberOfMode).toMutableList()
    }

    /**
     * Updates correct and wrong percentages
     *
     * @param good the number of correct guesses
     * @param bad the number of wrong guesses
     * @return a pair of newly calculated <correctPercent, wrongPercent>
     */
    private fun correctnessPercentUpdate(good: Int, bad: Int): Pair<Float, Float> {
        val total = good + bad
        val goodPercent = (good * 100 / total).toFloat()
        val badPercent = 100 - goodPercent
        return goodPercent to badPercent
    }

    /**
     * Updates correct/wrong count and calls correctnessPercentUpdate
     *
     * @param correct true if guess was correct, false if wrong
     * @param mode the current game mode
     */
    fun correctnessUpdate(correct: Int, wrong: Int, mode: Mode) {
        correctArray[mode.ordinal] += correct
        wrongArray[mode.ordinal] += wrong
        val (a, b) = correctnessPercentUpdate(correctArray[mode.ordinal], wrongArray[mode.ordinal])
        correctPercent[mode.ordinal] = a
        wrongPercent[mode.ordinal] = b
    }

    /**
     * Updates win/draw/loss count and percentages
     *
     * @param result the result of the game
     * @param mode the current mode
     */
    fun multiWinLossCountUpdate(result: Result, mode: Mode) {
        if (result == Result.WIN) {
            wins[mode.ordinal]++
        } else if (result == Result.DRAW) {
            draws[mode.ordinal]++
        } else {
            losses[mode.ordinal]++
        }
        val total = wins[mode.ordinal] + draws[mode.ordinal] + losses[mode.ordinal]
        winPercent[mode.ordinal] = (wins[mode.ordinal].toFloat() / total.toFloat() * hundredPercent)
        drawPercent[mode.ordinal] = (draws[mode.ordinal].toFloat() / total.toFloat() * hundredPercent)
        lossPercent[mode.ordinal] = 100F - winPercent[mode.ordinal] - drawPercent[mode.ordinal]
    }


    /**
     * Updates ELO
     *
     * @param opponentElo the ELO of the opponent
     */
    fun eloUpdate(result: Result, opponentElo: Int) {
        if (elo < 0){
            elo = 0;
        }
        elo = if (opponentElo == elo) {
                equalElo(result)
            } else if (opponentElo > elo) {
                smallerElo(result, opponentElo)
            } else {
                greaterElo(result, opponentElo)
            }

    }

    /**
     * Calculates ELO in case of ELO equality
     *
     * @param result the result of the game
     * @return the new elo of the user
     */
    private fun equalElo(result: Result): Int {
        if (result == Result.LOSS) {
            return if (elo < standardEqualValue) {
                0
            } else {
                elo - standardEqualValue
            }
        } else if (result == Result.WIN) {
            return elo + standardEqualValue
        }
        return elo
    }

    /**
     * Calculates ELO in case of elo is smaller than the opponent's
     *
     * @param result the result of the game
     * @return the new elo of the user
     */
    private fun smallerElo(result: Result, opponentElo: Int): Int {
        var ratio = 0 - (opponentElo.toFloat()/ elo.toFloat())
        if (result == Result.LOSS) {
            ratio = 0 - ((1/ratio).pow(3))
        } else if (result == Result.WIN) {
            ratio = 0 - (ratio).pow(2)
        }
        val diff = round((ratio * standardEqualValue)).toInt()
        return if (elo - diff <= 0) {
            0
        } else {
            elo - diff
        }
    }

    /**
     * Calculates ELO in case of elo is greater than the opponent's
     *
     * @param result the result of the game
     * @return the new elo of the user
     */
    private fun greaterElo(result: Result, opponentElo: Int): Int {
        var ratio = (elo.toFloat() / opponentElo.toFloat())
        if (result == Result.LOSS) {
            ratio = ratio.pow(2)
        } else if (result == Result.WIN)
            ratio = 0 - ((1/ratio).pow(3))
        val diff = round((ratio * standardEqualValue)).toInt()
        return if (elo - diff < 0) {
            0
        } else {
            elo - diff
        }
    }


    override fun toString(): String {
        return "hello " + elo
    }
}