package ch.epfl.sdp.blindwar.profile.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.menu.MainMenuActivity
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
class UserNewInfoFragmentTest : TestCase() {

    private val validPseudo = "validPseudo"
    private val invalidPseudo = "P"
    private val email = "test@bot.ch"
    private val password = "testtest"

    @Before
    fun setup() {
        Firebase.auth.signOut()
        Intents.init()
    }

    @After
    fun cleanup() {
        Intents.release()
    }

    @Test
    fun testSetBirthdate() {
        launchFragmentInContainer<UserNewInfoFragment>()
        onView(withId(R.id.NU_select_birthdate))
            .perform(scrollTo())
            .perform(click())
        clickOn(android.R.string.ok)
        onView(withId(R.id.NU_reset_birthdate))
            .check(
                matches(
                    withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE
                    )
                )
            )
    }

    @Test
    fun testResetBirthdate() {
        launchFragmentInContainer<UserNewInfoFragment>()
        onView(withId(R.id.NU_select_birthdate))
            .perform(scrollTo())
            .perform(click())
        clickOn(android.R.string.ok)
        onView(withId(R.id.NU_reset_birthdate))
            .perform(scrollTo())
            .perform(click())
        onView(withId(R.id.NU_reset_birthdate))
            .check(
                matches(
                    withEffectiveVisibility(
                        ViewMatchers.Visibility.INVISIBLE
                    )
                )
            )
    }

    @Test
    fun testSetProfilePicture() {
        launchFragmentInContainer<UserNewInfoFragment>()
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val oldPackageName = device.currentPackageName
        onView(withId(R.id.NU_editProfilePicture))
            .perform(scrollTo())
            .perform(click())

        // Press back until we get back to our activity
        var currentPackageName: String
        do {
            device.pressBack()
            currentPackageName = device.currentPackageName
        } while (currentPackageName != oldPackageName)

//        no assert because of ...
    }

    @Test
    fun testResetProfilePicture() {
        launchFragmentInContainer<UserNewInfoFragment>()
        onView(withId(R.id.NU_resetProfilePicture))
            .perform(scrollTo())
            .perform(click())
        onView(withId(R.id.NU_resetProfilePicture))
            .check(
                matches(
                    withEffectiveVisibility(
                        ViewMatchers.Visibility.INVISIBLE
                    )
                )
            )
    }

    @Test
    fun clickConfirm_TEST() {
        val login: Task<AuthResult> = FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
        try {
            Tasks.await(login)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        launchFragmentInContainer<UserNewInfoFragment>()
        Thread.sleep(1000)
        onView(withId(R.id.NU_pseudo))
            .perform(scrollTo())
            .perform(replaceText("pjio009"))
    }

    @Test
    fun testConfirm() {
        val login: Task<AuthResult> = FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
        try {
            Tasks.await(login)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        launchFragmentInContainer<UserNewInfoFragment>()
        Thread.sleep(1000)
        onView(withId(R.id.NU_pseudo))
            .perform(scrollTo())
            .perform(replaceText(validPseudo))
        onView(withId(R.id.NU_Confirm_Btn))
            .perform(scrollTo())
            .perform(click())
        intended(hasComponent(MainMenuActivity::class.java.name))
    }

    @Test
    fun testConfirm_ko() {
        val login: Task<AuthResult> = FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
        try {
            Tasks.await(login)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        launchFragmentInContainer<UserNewInfoFragment>()
        Thread.sleep(1000)
        onView(withId(R.id.NU_pseudo))
            .perform(scrollTo())
            .perform(replaceText(invalidPseudo))
        onView(withId(R.id.NU_Confirm_Btn))
            .perform(scrollTo())
            .perform(click())
        assertDisplayed(R.string.new_user_wrong_pseudo_title)
        clickOn(android.R.string.ok)
    }
}