package ch.epfl.sdp.blindwar.game.multi

import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import org.junit.After
import org.junit.Before
import org.junit.Rule

class LeaderboardTest {
    @get:Rule
    var testRule = ActivityScenarioRule(
        MultiPlayerMenuActivity::class.java
    )

    @get:Rule
    var permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.RECORD_AUDIO)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    /*@Test //TODO
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
        testRule.scenario.onActivity {
            Espresso.onView(ViewMatchers.withId(R.id.leaderboardButton))
                .perform(ViewActions.click())
            Espresso.onView(ViewMatchers.withId(R.id.musicRecyclerView))
                .check(matches(isDisplayed()))
            FirebaseAuth.getInstance().signOut()
        }
    }*/
}