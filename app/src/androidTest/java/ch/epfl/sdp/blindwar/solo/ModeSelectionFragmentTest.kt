package ch.epfl.sdp.blindwar.solo

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.ui.solo.AnimatedModeSelectionFragment
import ch.epfl.sdp.blindwar.ui.solo.ModeSelectionFragment
import ch.epfl.sdp.blindwar.ui.solo.PlaylistSelectionFragment
import org.hamcrest.Matchers.allOf
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ModeSelectionFragmentTest {

    @Test
    fun testCorrectlySetLayout() {
        launchFragmentInContainer<AnimatedModeSelectionFragment>()
        /** Mode buttons **/
        onView(withId(R.id.regularButton_)).check(matches(isDisplayed()))
        onView(withId(R.id.raceButton_)).check(matches(isDisplayed()))
        onView(withId(R.id.survivalButton_)).check(matches(isDisplayed()))
    }

    @Test
    fun launchSurvivalMode() {
        launchFragmentInContainer<AnimatedModeSelectionFragment>()
        onView(withId(R.id.survival)).perform(click())
        onView(withId(R.id.playlist_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun launchRaceMode() {
        launchFragmentInContainer<AnimatedModeSelectionFragment>()
        onView(withId(R.id.raceButton_)).perform(click())
        onView(withId(R.id.playlist_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun launchClassicMode() {
        launchFragmentInContainer<AnimatedModeSelectionFragment>()
        onView((withId(R.id.classic))).perform(click())
        onView(withId(R.id.playlist_layout)).check(matches(isDisplayed()))
    }
}