package ch.epfl.sdp.blindwar

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import ch.epfl.sdp.blindwar.ui.ProfileActivity
import ch.epfl.sdp.blindwar.ui.SplashScreenActivity
import ch.epfl.sdp.blindwar.ui.StatisticsActivity
import ch.epfl.sdp.blindwar.ui.UserNewInfoActivity
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.ExecutionException


@RunWith(AndroidJUnit4::class)
class ProfileActivityTest : TestCase() {
    @get:Rule
    var testRule = ActivityScenarioRule(
        ProfileActivity::class.java
    )

    @Before
    fun setup() {
        Intents.init()
        Espresso.closeSoftKeyboard()
    }

    @After
    fun cleanup() {
        Intents.release()
    }

    @Test
    fun testStatisticsButton() {
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.statsButton))
            .perform(click())
        intended(hasComponent(StatisticsActivity::class.java.name))
    }

    @Test
    fun statisticsUpdatedCorrectly() {
        val testEmail = "test@test.test"
        val testPassword = "testTest"
        val login: Task<AuthResult> = FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(testEmail, testPassword)
        try {
            Tasks.await<AuthResult>(login)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        /*
        Thread.sleep(4000)
        Espresso.onView(ViewMatchers.withId(R.id.emailView))
            .check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString("test@bot.ch"))))*/
        Espresso.onView(ViewMatchers.withId(R.id.statsButton))
            .perform(ViewActions.click())
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.pressBack()
        Espresso.onView(ViewMatchers.withId(R.id.logoutButton)).perform(ViewActions.click())
    }

    @Test
    fun testLogoutButton() {
        onView(withId(R.id.logoutButton))
            .perform(click())
        intended(hasComponent(SplashScreenActivity::class.java.name))
    }

    @Test
    fun testEditProfileButton() {
        onView(withId(R.id.editProfileButton))
            .perform(click())
        intended(hasComponent(UserNewInfoActivity::class.java.name))
    }
}