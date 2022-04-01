package ch.epfl.sdp.blindwar.solo

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBackUnconditionally
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.domain.game.GameDifficulty
import ch.epfl.sdp.blindwar.domain.game.GameMode
import ch.epfl.sdp.blindwar.ui.solo.PlayActivity
import ch.epfl.sdp.blindwar.ui.tutorial.DemoActivity
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayActivityTest {
    @get:Rule
    var testRule = ActivityScenarioRule(
        PlayActivity::class.java
    )

    @Test
    fun testModeSelectionDisplayedOnLaunch() {
        onView(withId(R.id.regularButton_)).check(matches(isClickable()))
        onView(withId(R.id.survivalButton_)).check(matches(isClickable()))
        onView(withId(R.id.raceButton_)).check(matches(isClickable()))
    }

    @Test
    fun testRegularMode() {
        testLaunchMode(R.id.regularButton_)
    }

    @Test
    fun testSurvivalMode() {
        testLaunchMode(R.id.survivalButton_)
    }

    @Test
    fun testRaceMode() {
        testLaunchMode(R.id.raceButton_)
    }

    private fun testLaunchMode(btnId: Int) {
        onView(withId(btnId)).perform(click())
        testRule.scenario.onActivity {
            val observedMode = it.gameInstanceViewModel
                .gameInstance
                .value
                ?.gameConfig?.mode

            val expectedMode = when(btnId) {
                R.id.regularButton_ -> GameMode.REGULAR
                R.id.raceButton_ -> GameMode.TIMED
                else -> GameMode.SURVIVAL
            }
            assertEquals(expectedMode, observedMode)
        }
    }

    @Test
    fun testLaunchDemo() {
        launchDemoWithMode(R.id.regularButton_)
        onView(withId(R.id.guessButtonDemo)).check(matches(isDisplayed()))
        for (i in 0 until 3) {
            simulateLostRound()
        }

        testRule.scenario.onActivity {
            it.onBackPressed()
        }
    }

    private fun simulateLostRound() {
        Thread.sleep(GameDifficulty.DIFFICULT.timeToFind.toLong())
        pressBackUnconditionally()
    }

    private fun launchDemoWithMode(btnId: Int) {
        onView(withId(btnId)).perform(click())
        onView(withId(R.id.startButton)).perform(click())
    }
}