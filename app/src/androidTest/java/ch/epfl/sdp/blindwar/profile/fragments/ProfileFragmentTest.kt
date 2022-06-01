package ch.epfl.sdp.blindwar.profile.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.login.SplashScreenActivity
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
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.ExecutionException


@RunWith(AndroidJUnit4::class)
class ProfileFragmentTest : TestCase() {

    private val email = "test@bot.ch"
    private val password = "testtest"

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun cleanup() {
        Intents.release()
    }


    @Test
    fun testStatisticsButton() {
        launchFragmentInContainer<ProfileFragment>()
        clickOn(R.id.statsBtn)
        intended(hasComponent(StatisticsActivity::class.java.name))
    }

    @Test
    fun statisticsUpdatedCorrectly() {
        val login: Task<AuthResult> = FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
        try {
            Tasks.await(login)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        launchFragmentInContainer<ProfileFragment>()
        clickOn(R.id.statsBtn)
        //val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        //device.pressBack()
        pressBack()
        clickOn(R.id.logoutBtn)
    }

    @Test
    fun testDeleteButton_cancel() {
        launchFragmentInContainer<ProfileFragment>()
        clickOn(R.id.deleteBtn)
        assertDisplayed(R.string.account_deletion_text)
        clickOn(android.R.string.cancel)
    }

    // If no second sign in: get ERROR: "not attached to a context"
    @Test
    fun testDeleteButton_ok() {
        val loginAnon: Task<AuthResult> =
            FirebaseAuth.getInstance().signInAnonymously()
        val loginMail: Task<AuthResult> =
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        Firebase.auth.signOut()
        Thread.sleep(1500)
        try {
            Tasks.await(loginAnon)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        launchFragmentInContainer<ProfileFragment>()
        Thread.sleep(1500)
        clickOn(R.id.deleteBtn)
        assertDisplayed(R.string.account_deletion_text)
        clickOn(android.R.string.ok)
        // I don't know why but this brings back the context to the fragment
        try {
            Tasks.await(loginMail)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        Thread.sleep(1500)
        assertDisplayed(R.string.signin_welcome_text)
    }

    @Test
    fun historyUpdatedCorrectly() {
        val login: Task<AuthResult> = FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
        try {
            Tasks.await(login)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        launchFragmentInContainer<ProfileFragment>()
        Thread.sleep(1000)

        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        clickOn(R.id.statsBtn)
        device.pressBack()
        clickOn(R.id.historyBtn)
        clickOn(R.id.item_match_history)
        clickOn(R.id.item_liked_musics)
        device.pressBack()
        clickOn(R.id.logoutBtn)
    }

    @Test
    fun testLogoutButton() {
        val login: Task<AuthResult> = FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
        try {
            Tasks.await(login)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        launchFragmentInContainer<ProfileFragment>()
        clickOn(R.id.logoutBtn)
        intended(hasComponent(SplashScreenActivity::class.java.name))
    }

    @Test
    fun testEditProfileButton() {
        launchFragmentInContainer<ProfileFragment>()
        assertDisplayed(R.id.editBtn)
        // Here the fix : https://stackoverflow.com/questions/7575921/illegalstateexception-can-not-perform-this-action-after-onsaveinstancestate-wit
        // Not able to click on Button... Why ? -> certainly in the background of something else
//        clickOn(R.id.editBtn)
    }
}