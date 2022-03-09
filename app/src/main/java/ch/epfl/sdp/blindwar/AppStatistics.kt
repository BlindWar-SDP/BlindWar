package ch.epfl.sdp.blindwar

class AppStatistics {
    //statistics for solo mode
    var soloCorrect: Int = 0
    var soloWrong: Int = 0
    var soloCorrectPercent: Float = 0.0F
        private set
    var soloWrongPercent: Float = 0.0F
        private set

    //statistics for multiplayer mode
    var multiCorrect: Int = 0
    var multiWrong: Int = 0
    var multiCorrectPercent: Float = 0.0F
        private set
    var multiWrongPercent: Float = 0.0F
        private set
    var wins: Int = 0
    var losses: Int = 0
    var winPercent: Float = 0.0F
        private set
    var lossPercent: Float = 0.0F
        private set
    var elo: Int = 1000

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

    // function for updating solo percentages
    fun soloPercentUpdate() {
        var total = soloCorrect + soloWrong
        soloCorrectPercent = (soloCorrect * 100 / total).toFloat()
        soloWrongPercent = 100 - soloCorrectPercent
    }

    // function for updating solo percentages
    fun multiCorrectnessPercentUpdate() {
        var total = multiCorrect + multiWrong
        multiCorrectPercent = (multiCorrect * 100 / total).toFloat()
        multiWrongPercent = 100 - multiCorrectPercent
    }

    // function for updating solo percentages
    fun multiWinRateUpdate() {
        var total = wins + losses
        winPercent = (wins * 100 / total).toFloat()
        lossPercent = 100 - winPercent
    }

    // function for updating elo (very simplified version)
    fun eloUpdate(opponentElo: Int, win: Boolean) {
        if (win) {
            if (opponentElo > elo) {
                elo+= 15
            } else if (opponentElo == elo) {
                elo+= 10
            } else {
                elo+=5
            }
        } else {
            if (opponentElo > elo) {
                elo-= 5
            } else if (opponentElo == elo) {
                elo-= 10
            } else {
                elo-= 15
            }
        }
    }

}