package ch.epfl.sdp.blindwar.ui

class AppStatistics {
    private val numberOfMode = Mode.values().size

    var correctArray = IntArray(numberOfMode)
        private set
    var wrongArray = IntArray(numberOfMode)
        private set
    var correctPercent = FloatArray(numberOfMode)
        private set
    var wrongPercent = FloatArray(numberOfMode)
        private set
    var wins: Int = 0
        private set
    var losses: Int = 0
        private set
    var winPercent: Float = 0.0F
        private set
    var lossPercent: Float = 0.0F
        private set
    var elo: Int = 1000
        private set

    //function for resetting stats to 0 (except for elo)
    fun resetStatistics() {
        correctArray = IntArray(numberOfMode)
        wrongArray = IntArray(numberOfMode)
        correctPercent = FloatArray(numberOfMode)
        wrongPercent = FloatArray(numberOfMode)
        wins = 0
        losses = 0
        winPercent = 0.0F
        lossPercent = 0.0F
    }

    // general function for updating percentages
    private fun percentUpdate(good: Int, bad: Int): Pair<Float, Float> {
        val total = good + bad
        val goodPercent = (good * 100 / total).toFloat()
        val badPercent = 100 - goodPercent
        return goodPercent to badPercent
    }

    /**
     * function for updating correct/wrong stats
     *
     * @param correct
     */
    fun correctnessUpdate(correct: Boolean, mode: Mode) {
        if (correct) {
            correctArray[mode.ordinal]++
        } else {
            wrongArray[mode.ordinal]++
        }
        val (a, b) = percentUpdate(correctArray[mode.ordinal], wrongArray[mode.ordinal])
        correctPercent[mode.ordinal] = a
        wrongPercent[mode.ordinal] = b
    }

    // function for updating win/loss numbers
    fun multiWinLossCountUpdate(win: Boolean) {
        if (win) {
            wins++
        } else {
            losses++
        }
        val (a, b) = percentUpdate(wins, losses)
        winPercent = a
        lossPercent = b
    }


    // function for updating elo in case of win (very simple version)
    fun eloUpdateWin(opponentElo: Int) {
        elo += when {
            opponentElo > elo -> 15
            opponentElo == elo -> 10
            else -> 5
        }
    }

    // function for updating elo in case of loss (very simple version)
    fun eloUpdateLoss(opponentElo: Int) {
        elo -= when {
            opponentElo > elo -> 5
            opponentElo == elo -> 10
            else -> 15
        }
    }
}