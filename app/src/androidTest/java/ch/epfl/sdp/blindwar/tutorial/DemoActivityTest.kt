package ch.epfl.sdp.blindwar.tutorial

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.domain.game.SongMetaData
import ch.epfl.sdp.blindwar.ui.tutorial.DemoActivity
import org.junit.Rule
import ch.epfl.sdp.blindwar.R
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class DemoActivityTest {
    @get:Rule
    var testRule = ActivityScenarioRule(
        DemoActivity::class.java
    )

    @Test
    fun layoutVisibleAfterPressingBackOnSummary() {
        makeCorrectGuess()
        testRule.scenario.onActivity {
            it.onBackPressed()
        }
        checkLayoutVisibility(Visibility.VISIBLE)
        pressBackUnconditionally()
        testRule.scenario.onActivity {
            it.startActivity(Intent(ApplicationProvider.getApplicationContext(), DemoActivity::class.java))
        }
    }

    private fun checkLayoutVisibility(visibility: Visibility) {
        onView(withId(R.id.guessButton)).check(matches(withEffectiveVisibility(visibility)))
        onView(withId(R.id.guessEditText)).check(matches(withEffectiveVisibility(visibility)))
        onView(withId(R.id.scoreTextView)).check(matches(withEffectiveVisibility(visibility)))
        onView(withId(R.id.startButton)).check(matches(withEffectiveVisibility(visibility)))
    }

    private fun makeCorrectGuess() {
        /** TODO: Refactor Game class to avoid this encapsulation leak **/
        var correctMetadata = SongMetaData("", "", "")
        testRule.scenario.onActivity {
            correctMetadata = it.game.currentMetaData!!
        }

        //Log.d(TAG, correctMetadata.toString())
        onView(withId(R.id.guessEditText)).perform(clearText(), typeText(correctMetadata.title))
        onView(withId(R.id.guessButton)).perform(closeSoftKeyboard()).perform(click())
    }

    private fun makeBadGuess() {
        onView(withId(R.id.guessEditText))
            .perform(clearText(), typeText("THIS IS NOT CORRECT"))
        onView(withId(R.id.guessButton)).perform(closeSoftKeyboard()).perform(click())
    }

    @Test
    fun songSummaryDisplayedAfterCorrectGuess() {
        makeCorrectGuess()
        onView(withId(R.id.song_summary_fragment)).check(matches(isDisplayed()))
    }

    @Test
    fun layoutGoneAfterCorrectGuess() {
        makeCorrectGuess()
        checkLayoutVisibility(Visibility.GONE)
    }

    @Test
    fun correctGuessAfterBadGuess() {
        makeBadGuess()
        checkLayoutVisibility(Visibility.VISIBLE)
        makeCorrectGuess()
        checkLayoutVisibility(Visibility.GONE)
        testRule.scenario.onActivity {
            it.onBackPressed()
        }
        checkLayoutVisibility(Visibility.VISIBLE)
    }

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
        makeBadGuess()
        onView(withId(R.id.guessEditText)).check(matches(withText("")))
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
            //assertEquals(it.playin, is(false))
        }

        onView(withId(R.id.startButton)).perform(click())
        testRule.scenario.onActivity {
            //assertThat(it.playing, is(true))
        }
    }
}