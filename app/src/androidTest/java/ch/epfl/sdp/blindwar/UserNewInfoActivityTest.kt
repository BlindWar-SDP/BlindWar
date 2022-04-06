package ch.epfl.sdp.blindwar


import android.widget.Toast
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.ui.ProfileActivity
import ch.epfl.sdp.blindwar.ui.UserAdditionalInfoActivity
import ch.epfl.sdp.blindwar.ui.UserNewInfoActivity
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.ExecutionException
import kotlin.concurrent.thread


@RunWith(AndroidJUnit4::class)
class UserNewInfoActivityTest : TestCase() {

    private val strNotDefault = "notDefaultText"
    private val email = "test@test.test"
    private val password = "testTest"

    @get:Rule
    var testRule = ActivityScenarioRule(
        UserNewInfoActivity::class.java
    )

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun cleanup() {
        Intents.release()
//        testRule.scenario.onActivity {
//            val toast: Toast = it.TOAST_NAME //
//            toast?.let{
//                toast.cancel()
//            }
    }

    @Test
    fun testLayoutVisibility() {
        val visibleIds = listOf(
            R.id.NU_pseudo,
            R.id.NU_FirstName,
            R.id.NU_LastName,
            R.id.NU_additional_info,
            R.id.NU_Confirm_Btn
        )
        for (id in visibleIds) {
            onView(withId(id))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        }
    }

//    @Test
//    fun testConfirm_allGood() {
//        val login: Task<AuthResult> = FirebaseAuth.getInstance()
//            .signInWithEmailAndPassword(email, password)
//        try {
//            Tasks.await(login)
//        } catch (e: ExecutionException) {
//            e.printStackTrace()
//        } catch (e: InterruptedException) {
//            e.printStackTrace()
//        }
//
//        onView(withId(R.id.NU_pseudo))
//            .perform(replaceText("ValidPseudo"))
//        onView(withId(R.id.NU_Confirm_Btn))
//            .perform(click())
//        intended(IntentMatchers.hasComponent(ProfileActivity::class.java.name))
//    }

    @Test
    fun testConfirm_PseudoTooShort() {
        onView(withId(R.id.NU_pseudo))
            .perform(replaceText(""))
        onView(withId(R.id.NU_Confirm_Btn))
            .perform(click())
        assertDisplayed(R.string.new_user_wrong_pseudo_text)
    }

    @Test
    fun testConfirm_PseudoIsPseudo() {
        onView(withId(R.id.NU_pseudo))
            .perform(replaceText("Pseudo"))
        onView(withId(R.id.NU_Confirm_Btn))
            .perform(click())
        assertDisplayed(R.string.new_user_wrong_pseudo_text)
    }

    fun testAdditionalInfoBtn(){
        onView(withId(R.id.NU_additional_info))
            .perform(click())
        intended(IntentMatchers.hasComponent(UserAdditionalInfoActivity::class.java.name))
    }

    // =====================================
    // Delete default value when click on it
    @Test
    fun testClearPseudo() {
        val id = R.id.NU_pseudo
        onView(withId(id))
            .perform(
                replaceText("Pseudo"),
                click(),
                click(),
                closeSoftKeyboard()
            ) // TODO: Why should click twice to delete text
        onView(withId(id)).check(matches(withText("")))
    }

    @Test
    fun testClearFirstName() {
        val id = R.id.NU_FirstName
        onView(withId(id))
            .perform(
                replaceText("First Name"),
                click(), click(), closeSoftKeyboard())
        onView(withId(id)).check(matches(withText("")))
    }

    @Test
    fun testClearLastName() {
        val id = R.id.NU_LastName
        closeSoftKeyboard()
        onView(withId(id))
            .perform(
                replaceText("Last Name"),
                click(), click(), closeSoftKeyboard())
        onView(withId(id)).check(matches(withText("")))
    }

    // ========================================
    // Don't delete text when not default value
    @Test
    fun testNotClearPseudo() {
        val id = R.id.NU_pseudo
        onView(withId(id))
            .perform(replaceText(strNotDefault), closeSoftKeyboard())
        onView(withId(id)).check(matches(withText(strNotDefault)))
    }

    @Test
    fun testNotClearFirstName() {
        val id = R.id.NU_FirstName
        onView(withId(id))
            .perform(replaceText(strNotDefault), closeSoftKeyboard())
        onView(withId(id)).check(matches(withText(strNotDefault)))
    }

    @Test
    fun testNotClearLastName() {
        val id = R.id.NU_LastName
        onView(withId(id))
            .perform(replaceText(strNotDefault), closeSoftKeyboard())
        onView(withId(id)).check(matches(withText(strNotDefault)))
    }

    // check No Default Values:


}