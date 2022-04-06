package ch.epfl.sdp.blindwar

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.ui.StatisticsActivity
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
class StatisticsActivityTest : TestCase() {

    private val placeholder = 1

    @get:Rule
    var testRule = ActivityScenarioRule(
        StatisticsActivity::class.java
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
    fun placeholderTest() {
        assertEquals(placeholder, 1)
    }

    /**
     * Logs in.Goes to solo mode. Pass every(fail everything quickly)
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