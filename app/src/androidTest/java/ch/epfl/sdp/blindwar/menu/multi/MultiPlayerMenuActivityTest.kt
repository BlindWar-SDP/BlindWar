package ch.epfl.sdp.blindwar.menu.multi

import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindwar.game.multi.MultiPlayerMenuActivity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MultiPlayerMenuActivityTest {
    @get:Rule
    var testRule = ActivityScenarioRule(
        MultiPlayerMenuActivity::class.java
    )

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun onCreate() {
        //Placeholder
        assertEquals(1,1)
    }
}