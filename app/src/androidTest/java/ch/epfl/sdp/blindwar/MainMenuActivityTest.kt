package ch.epfl.sdp.blindwar

import android.view.View
import android.widget.ImageView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.ui.DemoSRActivity
import ch.epfl.sdp.blindwar.ui.MainMenuActivity
import ch.epfl.sdp.blindwar.ui.ProfileActivity
import ch.epfl.sdp.blindwar.ui.SplashScreenActivity
import ch.epfl.sdp.blindwar.ui.solo.SoloMenuActivity
import ch.epfl.sdp.blindwar.ui.tutorial.TutorialActivity
import com.google.firebase.auth.FirebaseAuth
import junit.framework.TestCase
import org.hamcrest.Matchers
import org.hamcrest.Matchers.containsString
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
        intended(hasComponent(SoloMenuActivity::class.java.name))
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
    fun testLogoutButton() {
        onView(withId(R.id.logoutButton))
            .perform(click())
        intended(hasComponent(SplashScreenActivity::class.java.name))
    }

    @Test
    fun testLaunchSRDemo() {
        onView(withId(R.id.SpeechButton)).perform(click())
        intended(hasComponent(DemoSRActivity::class.java.name))
    }
/*
    @Test
    fun testUserProfile() {
        val testEmail = "test@bot.ch"
        val testPassword = "testtest"
        /*
        fun ViewInteraction.isDisplayed(): Boolean {
            try {
                check(matches(ViewMatchers.isDisplayed()))
                return true
            } catch (e: NoMatchingViewException) {
                return false
            }
        }*/
        onView(ViewMatchers.withId(R.id.logoutButton)).perform(ViewActions.click())
        var isDisplayed = false
        /*
        while (!isDisplayed) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(testEmail, testPassword)
            Thread.sleep(1000)
            isDisplayed = onView(ViewMatchers.withId(R.id.profileButton)).isDisplayed()
        }*/
        FirebaseAuth.getInstance().signInWithEmailAndPassword(testEmail, testPassword)
        Thread.sleep(1000)
        onView(ViewMatchers.withId(R.id.profileButton))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.emailView))
            .check(matches(ViewMatchers.withText(Matchers.containsString("test@bot.ch"))))
        onView(ViewMatchers.withId(R.id.logoutButton)).perform(ViewActions.click())
    } */
}