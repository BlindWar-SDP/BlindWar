package ch.epfl.sdp.blindwar.game.solo.fragments

import androidx.fragment.app.testing.launchFragment
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.R
import junit.framework.TestCase
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScoreFragmentTest : TestCase() {

    @Test
    fun test() {
        launchFragment<ScoreFragment>()

        try {
            Espresso.onView(withId(R.id.result_scoreboard))
                .check(matches(not(isDisplayed())))
        } catch (e: Exception) {
            assertTrue(e.javaClass.name == "androidx.test.espresso.NoMatchingViewException")
        }
    }
}