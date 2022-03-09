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

    //function for updating solo correct/wrong stats
    fun soloCorrectnessUpdate(correct: Boolean) {
        if (correct) {
            soloCorrect++
            soloPercentUpdate()
        } else {
            soloWrong++
            soloPercentUpdate()
        }
    }

    // function for updating solo percentages
    private fun soloPercentUpdate() {
        var total = soloCorrect + soloWrong
        soloCorrectPercent = (soloCorrect * 100 / total).toFloat()
        soloWrongPercent = 100 - soloCorrectPercent
    }

    //function for updating multiplayer correct/wrong stats
    fun multiCorrectnessUpdate(correct: Boolean) {
        if (correct) {
            multiCorrect++
            multiCorrectnessPercentUpdate()
        } else {
            multiWrong++
            multiCorrectnessPercentUpdate()
        }
    }

    // function for updating multiplayer percentages
    private fun multiCorrectnessPercentUpdate() {
        var total = multiCorrect + multiWrong
        multiCorrectPercent = (multiCorrect * 100 / total).toFloat()
        multiWrongPercent = 100 - multiCorrectPercent
    }

    // function for updating win/loss numbers
    fun multiWinLossCountUpdate(win: Boolean) {
        if (win) {
            wins++
            multiWinRateUpdate()
        } else {
            losses++
            multiWinRateUpdate()
        }
    }

    // function for updating multiplayer win rate
    private fun multiWinRateUpdate() {
        var total = wins + losses
        winPercent = (wins * 100 / total).toFloat()
        lossPercent = 100 - winPercent
    }

    // function for updating elo (very simple version)
    fun eloUpdate(opponentElo: Int, win: Boolean) {
        if (win) {
            elo += when {
                opponentElo > elo -> {
                    15
                }
                opponentElo == elo -> {
                    10
                }
                else -> {
                    5
                }
            }
        } else {
            elo -= when {
                opponentElo > elo -> {
                    5
                }
                opponentElo == elo -> {
                    10
                }
                else -> {
                    15
                }
            }
        }
    }

}