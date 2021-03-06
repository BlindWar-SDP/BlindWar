package ch.epfl.sdp.blindwar.game.util

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

    fun testCloseCorrectTitleKeyboard() {
        assertTrue(gameHelper.isTheCorrectTitle("Les lacasd", "les lacs", false))
    }

    fun testCloseCorrectTitleVocal() {
        assertTrue(
            gameHelper.isTheCorrectTitle(
                "bohemian rhapsody de Queen",
                "bohemian rhapsody Queen",
                true
            )
        )
    }
}