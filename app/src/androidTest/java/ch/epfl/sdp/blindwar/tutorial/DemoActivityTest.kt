package ch.epfl.sdp.blindwar.tutorial

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.ui.tutorial.DemoActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.domain.game.SongMetaData

@RunWith(AndroidJUnit4::class)
class DemoActivityTest {
    @get:Rule
    var testRule = ActivityScenarioRule(
        DemoActivity::class.java
    )

    @Test
    fun isPlayClickable() {
        onView(withId(R.id.startButton)).check(matches(isClickable()))
    }

    @Test
    fun isGuessClickable() {
        onView(withId(R.id.guessButton)).check(matches(isClickable()))
    }

    @Test
    fun guessResetAfterClick() {
        onView(withId(R.id.guessEditText))
            .perform(clearText(), typeText("THIS IS NOT CORRECT"))
        onView(withId(R.id.guessButton)).perform(closeSoftKeyboard()).perform(click())
        onView(withId(R.id.guessEditText)).check(matches(withText("")))
    }

    @Test
    fun songSummaryLaunchedAfterCorrectGuess() {
        makeCorrectGuess()
        onView(withId(R.id.fragment_container))
    }

    /**
    @Test
    fun layoutVisibleAfterPressingBackOnSummary() {
    makeCorrectGuess()
    pressBack()
    onView(withId(R.id.guessButton)).check(matches(isDisplayed()))
    onView(withId(R.id.guessEditText)).check(matches(isDisplayed()))
    }
     **/

    private fun makeCorrectGuess() {
        val correctMetadata = SongMetaData("One More Time", "", "")
        onView(withId(R.id.guessEditText)).perform(clearText(), typeText(correctMetadata.title))
        onView(withId(R.id.guessButton)).perform(closeSoftKeyboard()).perform(click())
    }

    @Test
    fun guessResetAfterCorrectGuess() {
        makeCorrectGuess()
        onView(withId(R.id.guessEditText)).check(matches(withText("")))
    }

    @Test
    fun pauseGameTest() {
        onView(withId(R.id.startButton)).perform(click())
        testRule.scenario.onActivity {
            //assertThat(it.playing, is(false))
        }

        onView(withId(R.id.startButton)).perform(click())
        testRule.scenario.onActivity {
            //assertThat(it.playing, is(true))
        }
    }
}