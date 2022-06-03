package ch.epfl.sdp.blindwar.game.model.config

import junit.framework.TestCase

class GameConfigTest : TestCase() {

    fun testCreateNull() {
        val g = GameConfig(null, null)
        assertTrue(g.mode == null && g.parameter == null)
    }

    fun testCreateParameter() {
        val g = GameConfig(null, GameParameter())
        assertTrue(g.mode == null && g.parameter == GameParameter())
    }

    fun testCreateMode() {
        val g = GameConfig(GameMode.REGULAR, null)
        assertTrue(g.mode == GameMode.REGULAR && g.parameter == null)
    }

    fun testCreatePerfect() {
        val g = GameConfig(GameMode.REGULAR, GameParameter())
        assertTrue(g.mode == GameMode.REGULAR && g.parameter == GameParameter())
    }

}