package ch.epfl.sdp.blindwar.game.multi

import ch.epfl.sdp.blindwar.game.multi.model.Match
import junit.framework.TestCase
import org.junit.Test

class MatchTest: TestCase() {
    private var dummyPlayerList = mutableListOf("yeet1", "yeet2")
    private var dummyPseudoList = mutableListOf("yeet3", "yeet4")
    private var dummyResultList = mutableListOf(2, 3)
    private var dummyMatch = Match(
        "uid",
        1,
        dummyPlayerList,
        dummyPseudoList,
        null,
        dummyResultList,
        4
    )

    @Test
    fun testMatch() {
        assertEquals("uid", dummyMatch.uid)
        assertEquals(1, dummyMatch.elo)
        assertEquals(dummyPlayerList, dummyMatch.listPlayers)
        assertEquals(dummyPseudoList, dummyMatch.listPseudo)
        assertNull(dummyMatch.game)
        assertEquals(dummyResultList, dummyMatch.listResult)
        assertEquals(4, dummyMatch.maxPlayer)
    }
}