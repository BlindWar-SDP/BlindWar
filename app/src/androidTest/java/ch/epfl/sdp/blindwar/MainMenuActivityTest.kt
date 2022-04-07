package ch.epfl.sdp.blindwar

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.ui.DemoSRActivity
import ch.epfl.sdp.blindwar.ui.MainMenuActivity
import ch.epfl.sdp.blindwar.ui.ProfileActivity
import ch.epfl.sdp.blindwar.ui.multi.MultiPlayerActivity
import ch.epfl.sdp.blindwar.ui.solo.PlayActivity
import ch.epfl.sdp.blindwar.ui.tutorial.TutorialActivity
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import junit.framework.TestCase
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.ExecutionException


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
        intended(hasComponent(PlayActivity::class.java.name))
    }

    @Test
    fun testMultiButton() {
        onView(withId(R.id.multiButton))
            .perform(click())
        intended(hasComponent(MultiPlayerActivity::class.java.name))
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
    fun testLaunchSRDemo() {
        onView(withId(R.id.SpeechButton)).perform(click())
        intended(hasComponent(DemoSRActivity::class.java.name))
    }

    @Test
    fun testUserProfile() {
        val testEmail = "test@test.test"
        val testPassword = "testTest"
        val login: Task<AuthResult> = FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(testEmail, testPassword)
        try {
            Tasks.await(login)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        Thread.sleep(1000)
        onView(withId(R.id.profileButton))
            .perform(click())

        onView(withId(R.id.emailView))
            .perform(ViewActions.closeSoftKeyboard())
        Thread.sleep(1000)
        onView(withId(R.id.emailView))
            .check(matches(ViewMatchers.withText(Matchers.containsString(testEmail))))
        onView(withId(R.id.logoutButton)).perform(ViewActions.click())
    }
}