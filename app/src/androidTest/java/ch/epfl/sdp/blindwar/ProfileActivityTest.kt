package ch.epfl.sdp.blindwar

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import ch.epfl.sdp.blindwar.ui.ProfileActivity
import ch.epfl.sdp.blindwar.ui.SplashScreenActivity
import ch.epfl.sdp.blindwar.ui.StatisticsActivity
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ProfileActivityTest : TestCase() {
    @get:Rule
    var testRule = ActivityScenarioRule(
        ProfileActivity::class.java
    )

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun cleanup() {
        Intents.release()
    }

    /**
    @Test
    fun testLogoutButton() {
        onView(withId(R.id.logoutButton))
            .perform(click())
        Thread.sleep(1000)
        intended(hasComponent(SplashScreenActivity::class.java.name))
    }
    **/
    /*
    @Test
    fun testLoginButton() {
        onView(withId(R.id.backToMainButton))
            .perform(click())
        intended(hasComponent(MainMenuActivity::class.java.name))
    } */

    @Test
    fun testChooseImage() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val oldPackageName = device.currentPackageName

        onView(withId(R.id.editProfileButton))
            .perform(click())

        // Press back until we get back to our activity
        var currentPackageName: String
        do {
            device.pressBack()
            currentPackageName = device.currentPackageName
        } while (currentPackageName != oldPackageName)

        onView(withId(R.id.statsButton))
            .perform(click())
        intended(hasComponent(StatisticsActivity::class.java.name))
    }

    @Test
    fun testStatisticsButton() {
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.statsButton))
            .perform(click())
        intended(hasComponent(StatisticsActivity::class.java.name))
    }

}