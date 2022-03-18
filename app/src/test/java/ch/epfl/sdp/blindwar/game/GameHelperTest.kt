package ch.epfl.sdp.blindwar.game

import junit.framework.TestCase
import org.junit.Test

class GameHelperTest : TestCase() {

    private val gameHelper = GameHelper

    @Test
    fun correctTitleVocal() {
        assertTrue(gameHelper.isTheCorrectTitle("connemara", "connemara", true))
    }

    @Test
    fun slightlyCorrectTitleVocal() {
        assertTrue(
            gameHelper.isTheCorrectTitle(
                "Les lacs du connemara",
                "Les lacs connemara",
                true
            )
        )
    }

    @Test
    fun falseTitle() {
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

    @Test
    fun falseTitleVocal() {
        assertFalse(
            gameHelper.isTheCorrectTitle(
                "Les lacs du connemara",
                "Les lacs du kirghistan timor oriental",
                true
            )
        )
    }

    @Test
    fun falseTitleKeyboard() {
        assertFalse(
            gameHelper.isTheCorrectTitle(
                "Les lacs du connemara",
                "connemara les lacs ",
                false
            )
        )
    }

    @Test
    fun correctTitleKeyboard() {
        assertTrue(
            gameHelper.isTheCorrectTitle(
                "connemara a a a a a a a a a a ",
                "connemara a a a a a a a a b",
                false
            )
        )
    }
}