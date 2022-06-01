package ch.epfl.sdp.blindwar.game.multi

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindwar.R
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.ExecutionException

class LeaderboardTest {
    @get:Rule
    var testRule = ActivityScenarioRule(
        MultiPlayerMenuActivity::class.java
    )

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testShowLeaderboard() {
        val testEmail = "test@bot.ch"
        val testPassword = "testtest"
        val login: Task<AuthResult> = FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(testEmail, testPassword)
        try {
            Tasks.await(login)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        Espresso.onView(ViewMatchers.withId(R.id.leaderboardButton))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.musicRecyclerView)).check(matches(isDisplayed()))
        FirebaseAuth.getInstance().signOut()
    }
}