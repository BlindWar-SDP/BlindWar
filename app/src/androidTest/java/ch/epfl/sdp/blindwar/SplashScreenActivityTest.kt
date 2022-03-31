package ch.epfl.sdp.blindwar


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.ui.SplashScreenActivity
import com.google.firebase.auth.FirebaseAuth
import junit.framework.TestCase
import org.hamcrest.Matchers.containsString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


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
    fun testUserProfile() {
        val testEmail = "test@bot.ch"
        val testPassword = "testtest"
        FirebaseAuth.getInstance().signInWithEmailAndPassword(testEmail, testPassword)
        onView(withId(R.id.profileButton)).perform(click())
        onView(withId(R.id.eloDeclarationView)).check(matches(withText(containsString("1000"))))
        onView(withId(R.id.emailView)).check(matches(withText(containsString("test@bot.ch"))))
        onView(withId(R.id.logoutButton)).perform(click())
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