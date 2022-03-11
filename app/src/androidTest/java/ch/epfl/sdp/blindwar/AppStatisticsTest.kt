package ch.epfl.sdp.blindwar

import org.junit.Test

class AppStatisticsTest {

    private var testStats = AppStatistics() // for individual metrics
    private var testStats2 = AppStatistics() // for right/win percentage
    private var testStats3 = AppStatistics() // for wrong/loss percentage
    private var testStats4 = AppStatistics() // for reset test
    private var testStats5 = AppStatistics() // for elo test
    private val modeSize = Mode.values().size
    private val soloIndex = Mode.SOLO.ordinal
    private val multiIndex = Mode.MULTI.ordinal

    @Test
    fun getCorrectInitial() {
        assert(testStats.correctArray.contentEquals(IntArray(modeSize)))
    }

    @Test
    fun getSoloCorrectInitial() {
        assert(testStats.correctArray[soloIndex] == 0)
    }

    @Test
    fun soloCorrectnessUpdateCorrect() {
        testStats.correctnessUpdate(true, Mode.SOLO)
        assert(testStats.correctArray[soloIndex] == 1)
    }

    @Test
    fun getWrongInitial() {
        assert(testStats.wrongArray.contentEquals(IntArray(modeSize)))
    }

    @Test
    fun getSoloWrongInitial() {
        assert(testStats.wrongArray[soloIndex] == 0)
    }

    @Test
    fun soloCorrectnessUpdateWrong() {
        testStats.correctnessUpdate(false, Mode.SOLO)
        assert(testStats.wrongArray[soloIndex] == 1)
    }

    @Test
    fun getSoloCorrectPercent() {
        testStats2.correctnessUpdate(true, Mode.SOLO)
        testStats2.correctnessUpdate(false, Mode.SOLO)
        assert(testStats2.correctPercent[soloIndex] == 50.0F)
    }

    @Test
    fun getSoloWrongPercent() {
        testStats3.correctnessUpdate(true, Mode.SOLO)
        testStats3.correctnessUpdate(false, Mode.SOLO)
        assert(testStats3.wrongPercent[soloIndex] == 50.0F)
    }

    @Test
    fun getMultiCorrectInitial() {
        assert(testStats.correctArray[multiIndex] == 0)
    }

    @Test
    fun multiCorrectnessUpdateCorrect() {
        testStats.correctnessUpdate(true, Mode.MULTI)
        assert(testStats.correctArray[multiIndex] == 1)
    }

    @Test
    fun getMultiWrongInitial() {
        assert(testStats.wrongArray[multiIndex] == 0)
    }

    @Test
    fun multiCorrectnessUpdateWrong() {
        testStats.correctnessUpdate(false, Mode.MULTI)
        assert(testStats.wrongArray[multiIndex] == 1)
    }

    @Test
    fun getMultiCorrectPercent() {
        testStats2.correctnessUpdate(true, Mode.MULTI)
        testStats2.correctnessUpdate(false, Mode.MULTI)
        assert(testStats2.correctPercent[multiIndex] == 50.0F)
    }

    @Test
    fun getMultiWrongPercent() {
        testStats3.correctnessUpdate(true, Mode.MULTI)
        testStats3.correctnessUpdate(false, Mode.MULTI)
        assert(testStats3.correctPercent[multiIndex] == 50.0F)
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
        testStats4.correctnessUpdate(true, Mode.SOLO)
        testStats4.correctnessUpdate(false, Mode.SOLO)
        testStats4.correctnessUpdate(true, Mode.MULTI)
        testStats4.correctnessUpdate(false, Mode.MULTI)
        testStats4.multiWinLossCountUpdate(true)
        testStats4.multiWinLossCountUpdate(false)
        testStats4.resetStatistics()
        assert(testStats4.correctArray[soloIndex] == 0)
        assert(testStats4.wrongArray[soloIndex] == 0)
        assert(testStats4.correctPercent[soloIndex] == 0.0F)
        assert(testStats4.wrongPercent[soloIndex] == 0.0F)
        assert(testStats4.correctArray[multiIndex] == 0)
        assert(testStats4.wrongArray[multiIndex] == 0)
        assert(testStats4.correctPercent[multiIndex] == 0.0F)
        assert(testStats4.wrongPercent[multiIndex] == 0.0F)
        assert(testStats4.wins == 0)
        assert(testStats4.losses == 0)
        assert(testStats4.winPercent == 0.0F)
        assert(testStats4.lossPercent == 0.0F)
    }


    @Test
    fun eloUpdateWin() {
        testStats5.eloUpdateWin(1100)
        assert(testStats5.elo == 1015)
        testStats5.eloUpdateWin(1015)
        assert(testStats5.elo == 1025)
        testStats5.eloUpdateWin(1015)
        assert(testStats5.elo == 1030)
    }

    @Test
    fun eloUpdateLoss() {
        testStats4.eloUpdateLoss(1100)
        assert(testStats4.elo == 995)
        testStats4.eloUpdateLoss(995)
        assert(testStats4.elo == 985)
        testStats4.eloUpdateLoss(900)
        assert(testStats4.elo == 970)
    }
}