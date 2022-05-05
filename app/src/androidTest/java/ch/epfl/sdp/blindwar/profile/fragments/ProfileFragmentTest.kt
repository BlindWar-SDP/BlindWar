package ch.epfl.sdp.blindwar.profile.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.login.SplashScreenActivity
import ch.epfl.sdp.blindwar.login.UserNewInfoActivity
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.ExecutionException


@RunWith(AndroidJUnit4::class)
class ProfileFragmentTest : TestCase() {

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun cleanup() {
        Intents.release()
    }

    @Test
    fun placeholder() {
        assert(true)
    }


    @Test
    fun testStatisticsButton() {
        launchFragmentInContainer<ProfileFragment>()
            closeSoftKeyboard()
            onView(withId(R.id.statsBtn))
                .perform(click())
            intended(hasComponent(StatisticsActivity::class.java.name))
    }

    @Test
    fun statisticsUpdatedCorrectly() {
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

        /*
        Thread.sleep(4000)
        Espresso.onView(ViewMatchers.withId(R.id.emailView))
            .check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString("test@bot.ch"))))*/

        launchFragmentInContainer<ProfileFragment>()
            onView(withId(R.id.statsBtn))
                .perform(click())
            //val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            //device.pressBack()
            pressBack()
            onView(withId(R.id.logoutBtn)).perform(click())
    }

    /**
    fun testDeleteButton_cancel() {
        launchFragmentInContainer<ProfileFragment>()
            closeSoftKeyboard()
            onView(withId(R.id.deleteBtn))
                .perform(click())
            BaristaVisibilityAssertions.assertDisplayed(R.string.account_deletion_text)
            clickOn(android.R.string.cancel)
    }

    @Test
    fun testDeleteButton_ok_cancel() {
        launchFragmentInContainer<ProfileFragment>()
            closeSoftKeyboard()
            onView(withId(R.id.deleteBtn))
                .perform(click())
            BaristaVisibilityAssertions.assertDisplayed(R.string.account_deletion_text)
            clickOn(android.R.string.ok)
            BaristaVisibilityAssertions.assertDisplayed(R.string.account_deletion_confirm_text)
            clickOn(android.R.string.cancel)
    }

    @Test
    fun testDeleteButton_ok_ok() {
        launchFragmentInContainer<ProfileFragment>()
            closeSoftKeyboard()
            onView(withId(R.id.deleteBtn))
                .perform(click())
            BaristaVisibilityAssertions.assertDisplayed(R.string.account_deletion_text)
            clickOn(android.R.string.ok)
            BaristaVisibilityAssertions.assertDisplayed(R.string.account_deletion_confirm_text)
            clickOn(android.R.string.ok)
            //BaristaVisibilityAssertions.assertDisplayed(R.string.deletion_success) // toast not detected
            intended(hasComponent(SplashScreenActivity::class.java.name))
    } **/

    @Test
    fun historyUpdatedCorrectly() {
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
        launchFragmentInContainer<ProfileFragment>()
        Thread.sleep(2000)
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        onView(withId(R.id.statsBtn))
            .perform(click())
        device.pressBack()
        onView(withId(R.id.historyBtn))
            .perform(click())
        onView(withId(R.id.item_match_history)).perform(click())
        onView(withId(R.id.item_liked_musics)).perform(click())
        device.pressBack()
        onView(withId(R.id.logoutBtn)).perform(click())
    }

//    fun testDeleteButton_cancel() {
//        launchFragmentInContainer<ProfileFragment>()
//            closeSoftKeyboard()
//            onView(withId(R.id.deleteProfile))
//                .perform(click())
//            BaristaVisibilityAssertions.assertDisplayed(R.string.account_deletion_text)
//            clickOn(android.R.string.cancel)
//    }
//
//    @Test
//    fun testDeleteButton_ok_cancel() {
//
//        launchFragmentInContainer<ProfileFragment>()
//            closeSoftKeyboard()
//            onView(withId(R.id.deleteProfile))
//                .perform(click())
//            BaristaVisibilityAssertions.assertDisplayed(R.string.account_deletion_text)
//            clickOn(android.R.string.ok)
//            BaristaVisibilityAssertions.assertDisplayed(R.string.account_deletion_confirm_text)
//            clickOn(android.R.string.cancel)
//    }
//
//    @Test
//    fun testDeleteButton_ok_ok() {
//
//        launchFragmentInContainer<ProfileFragment>()
//            closeSoftKeyboard()
//            onView(withId(R.id.deleteProfile))
//                .perform(click())
//            BaristaVisibilityAssertions.assertDisplayed(R.string.account_deletion_text)
//            clickOn(android.R.string.ok)
//            BaristaVisibilityAssertions.assertDisplayed(R.string.account_deletion_confirm_text)
//            clickOn(android.R.string.ok)
//            //BaristaVisibilityAssertions.assertDisplayed(R.string.deletion_success) // toast not detected
//            intended(hasComponent(SplashScreenActivity::class.java.name))
//    }


    @Test
    fun testLogoutButton() {
        launchFragmentInContainer<ProfileFragment>()
            closeSoftKeyboard()
            onView(withId(R.id.logoutBtn))
                .perform(click())
            intended(hasComponent(SplashScreenActivity::class.java.name))
    }

    @Test
    fun testEditProfileButton() {
        launchFragmentInContainer<ProfileFragment>()
        closeSoftKeyboard()
            onView(withId(R.id.editBtn))
                .perform(click())
            intended(hasComponent(UserNewInfoActivity::class.java.name))
    }
}