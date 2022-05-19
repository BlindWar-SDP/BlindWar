package ch.epfl.sdp.blindwar.profile.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import ch.epfl.sdp.blindwar.R
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
    }

    @Test
    fun testResetBirthdate() {
        Firebase.auth.signOut()
        launchFragmentInContainer<UserNewInfoFragment>()
        onView(withId(R.id.NU_reset_birthdate))
            .perform(scrollTo())
            .perform(click())
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
    }

    @Test
    fun testResetProfilePicture() {
        Firebase.auth.signOut()
        launchFragmentInContainer<UserNewInfoFragment>()
        onView(withId(R.id.NU_resetProfilePicture))
            .perform(scrollTo())
            .perform(click())
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
            .perform(replaceText(validPseudo))
        clickOn(R.id.NU_Confirm_Btn)
//        intended(hasComponent(MainMenuActivity::class.java.name))
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
            .perform(replaceText(invalidPseudo))
        clickOn(R.id.NU_Confirm_Btn)
//        assertDisplayed(R.string.new_user_wrong_pseudo_title)

    }
}