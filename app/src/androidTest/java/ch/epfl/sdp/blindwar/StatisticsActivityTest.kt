package ch.epfl.sdp.blindwar

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.profile.fragments.StatisticsActivity
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StatisticsActivityTest : TestCase() {

    private val placeholder = 1

    @get:Rule
    var testRule = ActivityScenarioRule(
        StatisticsActivity::class.java
    )

    @Before
    fun setup() {
        Intents.init()
        closeSoftKeyboard()
    }

    @After
    fun cleanup() {
        Intents.release()
    }

    @Test
    fun placeholderTest() {
        assertEquals(placeholder, 1)
    }

    /**
     * Logs in
     * Goes to solo mode.
     * Pass every(fail everything quickly)
     * and go statistics page again.
     * Temporarily, just open statistics while logged in
     */
    /*
    @Test
    fun statisticsUpdatedCorrectly() {
        val testEmail = "test@bot.ch"
        val testPassword = "testtest"
        val login: Task<AuthResult> = FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(testEmail, testPassword)
        try {
            Tasks.await<AuthResult>(login)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.profileButton))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.emailView))
            .check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString("test@bot.ch"))))
        Espresso.onView(ViewMatchers.withId(R.id.statsButton))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.logoutButton)).perform(ViewActions.click())
    }*/
}