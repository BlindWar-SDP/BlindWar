package ch.epfl.sdp.blindwar.tutorial

import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.ui.tutorial.TutorialFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TutorialFragmentTest {

    @Test
    fun testCorrectlySetLayout() {
        launchFragmentInContainer<TutorialFragment>()
        onView(withId(R.id.title)).check(matches(withText("Tutorial")))
        onView(withId(R.id.song_opt_title)).check(matches(withText("Game Options")))
        onView(withId(R.id.format_title)).check(matches(withText("Formats")))
        onView(withId(R.id.modes_title)).check(matches(withText("Modes")))
    }
}