package ch.epfl.sdp.blindwar


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.ui.MainMenuActivity
import ch.epfl.sdp.blindwar.ui.NewUserActivity
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewUserActivityTest : TestCase() {

    private val strNotDefault = "notDefaultText"

    @get:Rule
    var testRule = ActivityScenarioRule(
        NewUserActivity::class.java
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
    fun testLayoutVisibility() {
        val visible_ids = listOf<Int>(
            R.id.NU_pseudo,
            R.id.NU_FirstName,
            R.id.NU_LastName,
            R.id.NU_birthdate,
            R.id.NU_Confirm_Btn
        )
        for (id in visible_ids) {
            onView(withId(id))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        }
    }

    @Test
    fun testConfirm_allGood() {
        onView(withId(R.id.NU_pseudo))
            .perform(replaceText("ValidPseudo"))
        onView(withId(R.id.NU_Confirm_Btn))
            .perform(click())
        intended(IntentMatchers.hasComponent(MainMenuActivity::class.java.name))
    }

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

    @Test
    fun testBirthDateBtn() {
        onView(withId(R.id.NU_birthdate))
            .perform(click())
        // need to check that datePicker appear...
    }

    // =====================================
    // Delete default value when click on it
    @Test
    fun testClearPseudo() {
        val id = R.id.NU_pseudo
        onView(withId(id))
            .perform(
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
            .perform(click(), click(), closeSoftKeyboard())
        onView(withId(id)).check(matches(withText("")))
    }

    @Test
    fun testClearLastName() {
        val id = R.id.NU_LastName
        onView(withId(id))
            .perform(click(), click(), closeSoftKeyboard())
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