package ch.epfl.sdp.blindwar.game

import ch.epfl.sdp.blindwar.domain.game.GameHelper
import junit.framework.TestCase

class GameHelperTest : TestCase() {

    private val gameHelper = GameHelper

    fun testCorrectTitleVocal() {
        assertTrue(gameHelper.isTheCorrectTitle("connemara", "connemara", true))
    }

    fun testFalseTitle() {
        assertFalse(
            gameHelper.isTheCorrectTitle(
                "Les lacs du connemara",
                "connemara",
                true
            )
        )
        assertFalse(
            gameHelper.isTheCorrectTitle(
                "Les lacs du connemara",
                "connemara",
                false
            )
        )
    }

    fun testFalseTitleVocal() {
        assertFalse(
            gameHelper.isTheCorrectTitle(
                "Les lacs du connemara",
                "Les lacs du kirghistan timor oriental",
                true
            )
        )
    }

    fun testFalseTitleKeyboard() {
        assertFalse(
            gameHelper.isTheCorrectTitle(
                "Les lacs du connemara",
                "connemara les lacs ",
                false
            )
        )
    }
}