package ch.epfl.sdp.blindwar.tutorial

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DemoActivityTest {

    @get:Rule
    var testRule = ActivityScenarioRule(
        DemoActivity::class.java
    )

    @Test
    fun isPlayClickable() {
        Espresso.onView(withId(R.id.startButton)).check(matches(isClickable()))
    }

    @Test
    fun isGuessClickable() {
        Espresso.onView(withId(R.id.guessButton)).check(matches(isClickable()))
    }

    @Test
    fun guessResetAfterClick() {
        Espresso.onView(withId(R.id.guessEditText))
        .perform(clearText(), typeText("THIS IS NOT CORRECT"))
        Espresso.onView(withId(R.id.guessButton)).perform(closeSoftKeyboard()).perform(click())
        Espresso.onView(withId(R.id.guessEditText)).check(matches(withText("")))
    }
}