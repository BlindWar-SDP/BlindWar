package ch.epfl.sdp.blindwar.game.multi

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.util.GameActivity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChoseNumberOfPlayerActivityTest {
    @get:Rule
    var testRule = ActivityScenarioRule(
        ChoseNumberOfPlayerActivity::class.java
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
    fun testCancelButton() {
        onView(withId(R.id.cancel_number_player)).perform(ViewActions.click())
        intended(hasComponent(MultiPlayerMenuActivity::class.java.name))
    }

    @Test
    fun testCreateMatchButton() {
        onView(withId(R.id.create_match)).perform(ViewActions.click())
        intended(hasComponent(GameActivity::class.java.name))
    }

    @Test
    fun testDefaultNumberPicker() {
        onView(withParent(withId(R.id.number_of_players))).check(
            matches(
                withText(
                    ChoseNumberOfPlayerActivity.DEFAULT_NUMBER_OF_PLAYER.toString()
                )
            )
        )
    }
}