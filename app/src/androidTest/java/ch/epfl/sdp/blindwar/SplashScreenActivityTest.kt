package ch.epfl.sdp.blindwar

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.ui.SoloActivity
import ch.epfl.sdp.blindwar.ui.SplashScreenActivity
import androidx.test.espresso.assertion.ViewAssertions.matches
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SplashScreenActivityTest : TestCase() {
    @get:Rule
    var testRule = ActivityScenarioRule(
        SplashScreenActivity::class.java
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
    fun testOnCreate() {
        // What The FFF
//        if (UserAuth().isSignedIn()) {
//            Intents.intended(IntentMatchers.hasComponent(MainMenuActivityTest::class.java.name))
//        } else {
//            Intents.intended(IntentMatchers.hasComponent(NewUserActivityTest::class.java.name))
//        }
    }
}