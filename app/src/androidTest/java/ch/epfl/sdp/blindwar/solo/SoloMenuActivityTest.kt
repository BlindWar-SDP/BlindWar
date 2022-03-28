package ch.epfl.sdp.blindwar.solo

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.ui.*
import ch.epfl.sdp.blindwar.ui.solo.SoloMenuActivity
import ch.epfl.sdp.blindwar.ui.tutorial.TutorialActivity
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SoloMenuActivityTest : TestCase() {
    @get:Rule
    var testRule = ActivityScenarioRule(
        SoloMenuActivity::class.java
    )

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun cleanup() {
        Intents.release()
    }

    @Test
    fun testOnlineMusicButton() {
        onView(withId(R.id.onlineMusicButton)).check(matches(isClickable()));
    }

    @Test
    fun testLocalMusicButton() {
        onView(withId(R.id.localMusicButton)).check(matches(isClickable()));
    }

    @Test
    fun testTutorialMusicButton() {
        onView(withId(R.id.tutorialMusicButton)).check(matches(isClickable()));
    }

}