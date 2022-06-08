package ch.epfl.sdp.blindwar.login

import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.R
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.ExecutionException


@RunWith(AndroidJUnit4::class)
class SplashScreenActivityTest : TestCase() {

    private val email = "test@bot.ch"
    private val password = "testtest"

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
    fun testLoggedOut() {
        Firebase.auth.signOut()
        testRule.scenario.onActivity {
            it.startActivity(it.intent)
        }
        assertDisplayed(R.id.textView_signIn)
    }

    @Test
    fun testSignInGuest() {
        Firebase.auth.signOut()
        testRule.scenario.onActivity {
            it.startActivity(it.intent)
        }
        clickOn(R.id.Btn_anonymous)
        Thread.sleep(1000)
        assertDisplayed(R.id.titleText)
        clickOn(R.id.item_profile)
        clickOn(R.id.deleteBtn)
        clickOn(android.R.string.ok)
    }

    @Test
    fun testLoggedIn() {
        val login: Task<AuthResult> = FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
        try {
            Tasks.await(login)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        testRule.scenario.onActivity {
            it.startActivity(it.intent)
        }
        clickOn(R.id.item_profile)
        assertDisplayed(R.id.nameView)
    }
}
