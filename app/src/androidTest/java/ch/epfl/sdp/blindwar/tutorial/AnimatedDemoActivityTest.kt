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
import ch.epfl.sdp.blindwar.ui.tutorial.AnimatedDemoActivity
import ch.epfl.sdp.blindwar.ui.tutorial.DemoActivity
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AnimatedDemoActivityTest {
    @get:Rule
    var testRule = ActivityScenarioRule(
        AnimatedDemoActivity::class.java
    )

    private fun checkLayoutVisibility(visibility: Visibility) {
        onView(withId(R.id.startButton)).check(matches(withEffectiveVisibility(visibility)))
    }

    @Test
    fun pauseGameTest() {
        onView(withId(R.id.startButton)).perform(click())
        testRule.scenario.onActivity {
            assertFalse(it.playing)
        }

        onView(withId(R.id.startButton)).perform(click())
        testRule.scenario.onActivity {
            assertTrue(it.playing)
        }
    }

}