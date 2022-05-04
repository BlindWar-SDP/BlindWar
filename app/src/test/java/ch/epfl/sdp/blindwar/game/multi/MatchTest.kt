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

    private var emptyMatch = Match()

    @Test
    fun testMatchUID() {
        emptyMatch.uid = dummyMatch.uid
        assertEquals("uid", emptyMatch.uid)
    }

    @Test
    fun testMatchElo() {
        emptyMatch.elo = dummyMatch.elo
        assertEquals(1, emptyMatch.elo)
    }

    @Test
    fun testMatchPlayers() {
        emptyMatch.listPlayers = dummyMatch.listPlayers
        assertEquals(dummyPlayerList, emptyMatch.listPlayers)
    }

    @Test
    fun testMatchPseudos() {
        emptyMatch.listPseudo = dummyMatch.listPseudo
        assertEquals(dummyPseudoList, emptyMatch.listPseudo)
    }

    @Test
    fun testMatchResults() {
        emptyMatch.listResult = dummyMatch.listResult
        assertEquals(dummyResultList, emptyMatch.listResult)
    }

    @Test
    fun testMatchMaxPlayers() {
        emptyMatch.maxPlayer = dummyMatch.maxPlayer
        assertEquals(4, emptyMatch.maxPlayer)
    }

    @Test
    fun testNullGame() {
        assertNull(dummyMatch.game)
    }
}