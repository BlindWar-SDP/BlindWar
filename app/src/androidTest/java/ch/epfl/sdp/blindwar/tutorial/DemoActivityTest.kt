package ch.epfl.sdp.blindwar.tutorial

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.domain.game.SongMetaData
import ch.epfl.sdp.blindwar.domain.game.Tutorial
import ch.epfl.sdp.blindwar.ui.tutorial.DemoActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DemoActivityTest {
    @get:Rule
    var testRule = ActivityScenarioRule(
        DemoActivity::class.java
    )

    private val longString =
        "The mare flattened her ears against her skull and snorted throwing up earth with her hooves she didn't want to go" +
                "Marty didn't calm her with the Sign he jumped from the saddle and threw the reins over the horses head " +
                "He no longer had his old sword in its lizard skin sheath on his back; its place was filled with a shining " +
                "beautiful weapon with a cruciform and slender well-weighted hilt ending in a spherical pommel made of white metal" +
                "This time the gate didn't open for him It was already open, just as he had left it" +
                " He heard singing He didn't understand the words he couldn't even identify the language " +
                "He didn't need to the witcher felt and understood the very nature the essence of this quiet " +
                "piercing singing which flowed through the veins in a wave of nauseous overpowering menace"

    private val round = Tutorial
        .gameInstance
        .gameConfig
        .parameter
        .round

    private fun checkLayoutVisibility(visibility: Visibility) {
        onView(withId(R.id.guessButton)).check(matches(withEffectiveVisibility(visibility)))
        onView(withId(R.id.guessEditText)).check(matches(withEffectiveVisibility(visibility)))
        onView(withId(R.id.scoreTextView)).check(matches(withEffectiveVisibility(visibility)))
    }

    private fun makeCorrectGuess() {
        var correctMetadata = SongMetaData("", "", "")
        testRule.scenario.onActivity {
            correctMetadata = it.game.currentMetadata()!!
        }

        onView(withId(R.id.guessEditText))
            .perform(clearText(), typeText(correctMetadata.title), closeSoftKeyboard())
        onView(withId(R.id.guessButton)).perform(click())
    }

    private fun makeBadGuess() {
        onView(withId(R.id.guessEditText))
            .perform(clearText(), typeText("THIS IS NOT CORRECT"), closeSoftKeyboard())
        onView(withId(R.id.guessButton)).perform(click())
    }

    private fun makeGoodGuessGetBack() {
        makeCorrectGuess()
        testRule.scenario.onActivity {
            it.onBackPressed()
        }
    }

    @Test
    fun songSummaryDisplayedAfterCorrectGuess() {
        makeCorrectGuess()
        onView(withId(R.id.song_summary_fragment)).check(matches(isDisplayed()))
    }

    /** 30 seconds to guess **/
    @Test
    fun timeOutTest() {

        // Write in a edit text to ensure that screen doesn't lock
        for(i in 1..3) {
            onView(withId(R.id.guessEditText))
                .perform(clearText(), typeText("here"))
                .perform(closeSoftKeyboard())
            Thread.sleep(10000L)
        }
        Thread.sleep(100)

        onView(withId(R.id.song_summary_fragment)).check(matches(isDisplayed()))
    }

    @Test
    fun perfectGameTest() {
        for (i in 0 until round) makeGoodGuessGetBack()
        onView(withId(R.id.game_summary_fragment)).check(matches(isDisplayed()))
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
    fun layoutVisibleAfterPressingBackOnSummary() {
        makeCorrectGuess()
        testRule.scenario.onActivity {
            it.onBackPressed()
        }
        checkLayoutVisibility(Visibility.VISIBLE)
        pressBackUnconditionally()
        testRule.scenario.onActivity {
            it.startActivity(
                Intent(
                    ApplicationProvider.getApplicationContext(),
                    DemoActivity::class.java
                )
            )
        }
    }

    @Test
    fun layoutGoneAfterCorrectGuess() {
        makeCorrectGuess()
        checkLayoutVisibility(Visibility.GONE)
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
}