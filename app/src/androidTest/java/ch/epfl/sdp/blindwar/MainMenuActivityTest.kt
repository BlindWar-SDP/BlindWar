package ch.epfl.sdp.blindwar

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.tutorial.TutorialActivity
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainMenuActivityTest : TestCase() {
    @get:Rule
    var testRule = ActivityScenarioRule(
        MainMenuActivity::class.java
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
    fun testSoloButton() {
        onView(withId(R.id.soloButton))
            .perform(click())
        intended(hasComponent(SoloActivity::class.java.name))
    }

    @Test
    fun testTutorialButton() {
        onView(withId(R.id.tutorialButton))
            .perform(click())
        intended(hasComponent(TutorialActivity::class.java.name))
    }

    @Test
    fun testProfileButton() {
        onView(withId(R.id.profileButton))
            .perform(click())
        intended(hasComponent(ProfileActivity::class.java.name))
    }

    @Test
    fun testLaunchVosk() {
        onView(withId(R.id.SpeechButton)).perform(click())
        intended(hasComponent(VoskActivity::class.java.name))
    }
}