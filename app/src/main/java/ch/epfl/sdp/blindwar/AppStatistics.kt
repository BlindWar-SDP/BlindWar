package ch.epfl.sdp.blindwar

class AppStatistics {
    //statistics for solo mode
    var soloCorrect: Int = 0
        private set
    var soloWrong: Int = 0
        private set
    var soloCorrectPercent: Float = 0.0F
        private set
    var soloWrongPercent: Float = 0.0F
        private set

    //statistics for multiplayer mode
    var multiCorrect: Int = 0
        private set
    var multiWrong: Int = 0
        private set
    var multiCorrectPercent: Float = 0.0F
        private set
    var multiWrongPercent: Float = 0.0F
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
        soloCorrect = 0
        soloWrong = 0
        soloCorrectPercent = 0.0F
        soloWrongPercent = 0.0F
        multiCorrect = 0
        multiWrong = 0
        multiCorrectPercent = 0.0F
        multiWrongPercent = 0.0F
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

    //function for updating solo correct/wrong stats
    fun soloCorrectnessUpdate(correct: Boolean) {
        if (correct) {
            soloCorrect++
        } else {
            soloWrong++
        }
        val (a, b) = percentUpdate(soloCorrect, soloWrong)
        soloCorrectPercent = a
        soloWrongPercent = b
    }

    //function for updating multiplayer correct/wrong stats
    fun multiCorrectnessUpdate(correct: Boolean) {
        if (correct) {
            multiCorrect++
        } else {
            multiWrong++
        }
        val (a, b) = percentUpdate(multiCorrect, multiWrong)
        multiCorrectPercent = a
        multiWrongPercent = b
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