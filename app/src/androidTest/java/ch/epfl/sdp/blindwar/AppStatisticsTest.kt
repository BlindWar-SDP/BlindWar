package ch.epfl.sdp.blindwar

import ch.epfl.sdp.blindwar.profile.model.AppStatistics
import ch.epfl.sdp.blindwar.profile.model.Mode
import ch.epfl.sdp.blindwar.profile.model.Result
import junit.framework.TestCase.assertEquals
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
        assert(testStats.correctArray.toIntArray().contentEquals(IntArray(modeSize)))
    }

    @Test
    fun getSoloCorrectInitial() {
        assert(testStats.correctArray[soloIndex] == 0)
    }

    @Test
    fun soloCorrectnessUpdateCorrect() {
        testStats.correctnessUpdate(1, 0, Mode.SOLO)
        assert(testStats.correctArray[soloIndex] == 1)
    }

    @Test
    fun getWrongInitial() {
        assert(testStats.wrongArray.toIntArray().contentEquals(IntArray(modeSize)))
    }

    @Test
    fun getSoloWrongInitial() {
        assert(testStats.wrongArray[soloIndex] == 0)
    }

    @Test
    fun soloCorrectnessUpdateWrong() {
        testStats.correctnessUpdate(0, 1, Mode.SOLO)
        assert(testStats.wrongArray[soloIndex] == 1)
    }

    @Test
    fun getSoloCorrectPercent() {
        testStats2.correctnessUpdate(1,0,  Mode.SOLO)
        testStats2.correctnessUpdate(0, 1, Mode.SOLO)
        assert(testStats2.correctPercent[soloIndex] == 50.0F)
    }

    @Test
    fun getSoloWrongPercent() {
        testStats3.correctnessUpdate(1, 0,  Mode.SOLO)
        testStats3.correctnessUpdate(0, 1,  Mode.SOLO)
        assert(testStats3.wrongPercent[soloIndex] == 50.0F)
        
    }

    @Test
    fun getMultiCorrectInitial() {
        assert(testStats.correctArray[multiIndex] == 0)
    }

    @Test
    fun multiCorrectnessUpdateCorrect() {
        testStats.correctnessUpdate(1, 0, Mode.MULTI)
        assert(testStats.correctArray[multiIndex] == 1)
    }

    @Test
    fun getMultiWrongInitial() {
        assert(testStats.wrongArray[multiIndex] == 0)
    }

    @Test
    fun multiCorrectnessUpdateWrong() {
        testStats.correctnessUpdate(0, 1, Mode.MULTI)
        assert(testStats.wrongArray[multiIndex] == 1)
    }

    @Test
    fun getMultiCorrectPercent() {
        testStats2.correctnessUpdate(1, 0,  Mode.MULTI)
        testStats2.correctnessUpdate(0, 1,  Mode.MULTI)
        assert(testStats2.correctPercent[multiIndex] == 50.0F)
    }

    @Test
    fun getMultiWrongPercent() {
        testStats3.correctnessUpdate(1, 0, Mode.MULTI)
        testStats3.correctnessUpdate(0, 1,  Mode.MULTI)
        assert(testStats3.correctPercent[multiIndex] == 50.0F)
    }

    @Test
    fun getWinsInitial() {
        assert(testStats.wins[soloIndex] == 0)
        assert(testStats.wins[multiIndex] == 0)
    }

    @Test
    fun multiWinLossCountUpdateWin() {
        testStats.multiWinLossCountUpdate(Result.WIN, Mode.MULTI)
        assert(testStats.wins[multiIndex] == 1)
    }

    @Test
    fun getLossesInitial() {
        assert(testStats.losses[soloIndex] == 0)
        assert(testStats.losses[multiIndex] == 0)
    }

    @Test
    fun multiWinLossCountUpdateLoss() {
        testStats.multiWinLossCountUpdate(Result.LOSS, Mode.MULTI)
        assert(testStats.losses[multiIndex] == 1)
    }

    @Test
    fun getDrawsInitial() {
        assert(testStats.draws[soloIndex] == 0)
        assert(testStats.draws[multiIndex] == 0)
    }

    @Test
    fun multiWinLossCountUpdateDraw() {
        testStats.multiWinLossCountUpdate(Result.DRAW, Mode.MULTI)
        assert(testStats.draws[multiIndex] == 1)
    }

    @Test
    fun getWinPercent() {
        testStats2.multiWinLossCountUpdate(Result.WIN, Mode.MULTI)
        testStats2.multiWinLossCountUpdate(Result.LOSS, Mode.MULTI)
        assertEquals(50.0F, testStats2.winPercent[multiIndex])
    }

    @Test
    fun getLossPercent() {
        testStats3.multiWinLossCountUpdate(Result.WIN, Mode.MULTI)
        testStats3.multiWinLossCountUpdate(Result.LOSS, Mode.MULTI)
        assertEquals(50.0F, testStats3.lossPercent[multiIndex])
    }

    @Test
    fun getDrawPercent() {
        testStats3 = AppStatistics()
        testStats3.multiWinLossCountUpdate(Result.DRAW, Mode.MULTI)
        testStats3.multiWinLossCountUpdate(Result.LOSS, Mode.MULTI)
        assertEquals(50.0F, testStats3.drawPercent[multiIndex])
    }

    @Test
    fun getEloInitial() {
        assert(testStats.elo == 1000)
    }

    @Test
    fun resetStatistics() {
        testStats4.correctnessUpdate(1, 0, Mode.SOLO)
        testStats4.correctnessUpdate(0, 1, Mode.SOLO)
        testStats4.correctnessUpdate(1, 0, Mode.MULTI)
        testStats4.correctnessUpdate(0, 1, Mode.MULTI)
        testStats4.multiWinLossCountUpdate(Result.DRAW, Mode.MULTI)
        testStats4.multiWinLossCountUpdate(Result.WIN, Mode.MULTI)
        testStats4.multiWinLossCountUpdate(Result.LOSS, Mode.MULTI)
        testStats4.resetStatistics()
        assert(testStats4.correctArray[soloIndex] == 0)
        assert(testStats4.wrongArray[soloIndex] == 0)
        assert(testStats4.correctPercent[soloIndex] == 0.0F)
        assert(testStats4.wrongPercent[soloIndex] == 0.0F)
        assert(testStats4.correctArray[multiIndex] == 0)
        assert(testStats4.wrongArray[multiIndex] == 0)
        assert(testStats4.correctPercent[multiIndex] == 0.0F)
        assert(testStats4.wrongPercent[multiIndex] == 0.0F)

        assert(testStats4.wins[soloIndex] == 0)
        assert(testStats4.wins[multiIndex] == 0)
        assert(testStats4.draws[soloIndex] == 0)
        assert(testStats4.draws[multiIndex] == 0)
        assert(testStats4.losses[soloIndex] == 0)
        assert(testStats4.losses[multiIndex] == 0)

        assert(testStats4.winPercent[soloIndex] == 0.0F)
        assert(testStats4.winPercent[multiIndex] == 0.0F)
        assert(testStats4.drawPercent[soloIndex] == 0.0F)
        assert(testStats4.drawPercent[multiIndex] == 0.0F)
        assert(testStats4.lossPercent[soloIndex] == 0.0F)
        assert(testStats4.lossPercent[multiIndex] == 0.0F)

    }

    @Test
    fun eloUpdateNegative() {
        testStats5 = AppStatistics()
        testStats5.eloSetter(-2)
        testStats5.eloUpdate(Result.LOSS, 10)
        assertEquals(0, testStats5.elo)
    }


    @Test
    fun eloEqualLossNormal() {
        testStats5 = AppStatistics()
        testStats5.eloUpdate(Result.LOSS, 1000)
        assertEquals(992,testStats5.elo)
    }

    @Test
    fun eloEqualLossCorner() {
        testStats5 = AppStatistics()
        testStats5.eloSetter(6)
        testStats5.eloUpdate(Result.LOSS, 6)
        assertEquals(0,testStats5.elo)
    }

    @Test
    fun eloEqualDraw() {
        testStats5 = AppStatistics()
        testStats5.eloUpdate(Result.DRAW, 1000)
        assertEquals(1000,testStats5.elo)
    }

    @Test
    fun eloEqualWin() {
        testStats5 = AppStatistics()
        testStats5.eloUpdate(Result.WIN, 1000)
        assertEquals(1008,testStats5.elo)
    }

    @Test
    fun eloSmallerLossNormal() {
        testStats5 = AppStatistics()
        testStats5.eloUpdate(Result.LOSS, 1200)
        assertEquals(995,testStats5.elo)
    }

    @Test
    fun eloSmallerLossCorner() {
        testStats5 = AppStatistics()
        testStats5.eloSetter(2)
        testStats5.eloUpdate(Result.LOSS, 3)
        assertEquals(0,testStats5.elo)
    }

    @Test
    fun eloSmallerDraw() {
        testStats5 = AppStatistics()
        testStats5.eloUpdate(Result.DRAW, 1200)
        assertEquals(1010,testStats5.elo)
    }

    @Test
    fun eloSmallerWin() {
        testStats5 = AppStatistics()
        testStats5.eloUpdate(Result.WIN, 1200)
        assertEquals(1012,testStats5.elo)
    }

    @Test
    fun eloGreaterLossNormal() {
        testStats5 = AppStatistics()
        testStats5.eloUpdate(Result.LOSS, 800)
        assertEquals(988,testStats5.elo)
    }

    @Test
    fun eloGreaterLossCorner() {
        testStats5 = AppStatistics()
        testStats5.eloSetter(6)
        testStats5.eloUpdate(Result.LOSS, 5)
        assertEquals(0,testStats5.elo)
    }

    @Test
    fun eloGreaterDrawNormal() {
        testStats5 = AppStatistics()
        testStats5.eloUpdate(Result.DRAW, 800)
        assertEquals(990,testStats5.elo)
    }

    @Test
    fun eloGreaterDrawCorner() {
        testStats5 = AppStatistics()
        testStats5.eloSetter(6)
        testStats5.eloUpdate(Result.DRAW, 5)
        assertEquals(0,testStats5.elo)
    }

    @Test
    fun eloGreaterWinNormal() {
        testStats5 = AppStatistics()
        testStats5.eloUpdate(Result.WIN, 800)
        assertEquals(1004,testStats5.elo)
    }

    @Test
    fun toStringTest() {
        testStats5 = AppStatistics()
        assertEquals("hello 1000", testStats5.toString())
    }
}