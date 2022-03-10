package ch.epfl.sdp.blindwar

import org.junit.Test

class AppStatisticsTest {

    private var testStats = AppStatistics() // for individual metrics
    private var testStats2 = AppStatistics() // for right/win percentage
    private var testStats3 = AppStatistics() // for wrong/loss percentage

    @Test
    fun getSoloCorrectInitial() {
        assert(testStats.soloCorrect == 0)
    }

    @Test
    fun soloCorrectnessUpdateCorrect() {
        testStats.soloCorrectnessUpdate(true)
        assert(testStats.soloCorrect == 1)
    }

    @Test
    fun getSoloWrongInitial() {
        assert(testStats.soloWrong == 0)
    }

    @Test
    fun soloCorrectnessUpdateWrong() {
        testStats.soloCorrectnessUpdate(false)
        assert(testStats.soloWrong == 1)
    }

    @Test
    fun getSoloCorrectPercent() {
        testStats2.soloCorrectnessUpdate(true)
        testStats2.soloCorrectnessUpdate(false)
        assert(testStats2.soloCorrectPercent == 50.0F)
    }

    @Test
    fun getSoloWrongPercent() {
        testStats3.soloCorrectnessUpdate(true)
        testStats3.soloCorrectnessUpdate(false)
        assert(testStats3.soloCorrectPercent == 50.0F)
    }

    @Test
    fun getMultiCorrectInitial() {
        assert(testStats.multiCorrect == 0)
    }

    @Test
    fun multiCorrectnessUpdateCorrect() {
        testStats.multiCorrectnessUpdate(true)
        assert(testStats.multiCorrect == 1)
    }

    @Test
    fun getMultiWrongInitial() {
        assert(testStats.multiWrong == 0)
    }

    @Test
    fun multiCorrectnessUpdateWrong() {
        testStats.multiCorrectnessUpdate(false)
        assert(testStats.multiWrong == 1)
    }

    @Test
    fun getMultiCorrectPercent() {
        testStats2.multiCorrectnessUpdate(true)
        testStats2.multiCorrectnessUpdate(false)
        assert(testStats2.multiCorrectPercent == 50.0F)
    }

    @Test
    fun getMultiWrongPercent() {
        testStats3.multiCorrectnessUpdate(true)
        testStats3.multiCorrectnessUpdate(false)
        assert(testStats3.multiCorrectPercent == 50.0F)
    }

    @Test
    fun getWinsInitial() {
        assert(testStats.wins == 0)
    }

    @Test
    fun multiWinLossCountUpdateWin() {
        testStats.multiWinLossCountUpdate(true)
        assert(testStats.wins == 1)
    }

    @Test
    fun getLossesInitial() {
        assert(testStats.losses == 0)
    }

    @Test
    fun multiWinLossCountUpdateLoss() {
        testStats.multiWinLossCountUpdate(false)
        assert(testStats.losses == 1)
    }

    @Test
    fun getWinPercent() {
        testStats2.multiWinLossCountUpdate(true)
        testStats2.multiWinLossCountUpdate(false)
        assert(testStats2.winPercent == 50.0F)
    }

    @Test
    fun getLossPercent() {
        testStats3.multiWinLossCountUpdate(true)
        testStats3.multiWinLossCountUpdate(false)
        assert(testStats3.lossPercent == 50.0F)
    }

    @Test
    fun getEloInitial() {
        assert(testStats.elo == 1000)
    }

    @Test
    fun getEloAfterStrongWin() {
        assert(testStats.elo == 1000)
    }

    @Test
    fun resetStatistics() {
    }


    @Test
    fun eloUpdate() {
    }
}