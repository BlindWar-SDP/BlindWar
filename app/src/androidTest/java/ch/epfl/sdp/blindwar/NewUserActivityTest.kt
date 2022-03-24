package ch.epfl.sdp.blindwar

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.ui.MainMenuActivity
import ch.epfl.sdp.blindwar.ui.NewUserActivity
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewUserActivityTest : TestCase() {

    val strNotDefault = "notDefaultText"

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
    fun testConfirm() {
        onView(withId(R.id.NU_Confirm_Btn))
            .perform(click())
        intended(IntentMatchers.hasComponent(MainMenuActivity::class.java.name))
    }

    // =====================================
    // Delete default value when click on it
    @Test
    fun testClearPseudo() {
        val id=R.id.NU_pseudo
        onView(withId(id))
            .perform(click(), click()) // TODO: Why should click twice to delete text
        onView(withId(id)).check(matches(withText("")))
    }
    @Test
    fun testClearFirstName() {
        val id=R.id.NU_FirstName
        onView(withId(id))
            .perform(click(), click())
        onView(withId(id)).check(matches(withText("")))
    }
    @Test
    fun testClearLastName() {
        val id=R.id.NU_LastName
        onView(withId(id))
            .perform(click(), click())
        onView(withId(id)).check(matches(withText("")))
    }

    // ========================================
    // Don't delete text when not default value
    @Test
    fun testNotClearPseudo() {
        val id=R.id.NU_pseudo
        onView(withId(id))
            .perform(replaceText(strNotDefault), closeSoftKeyboard())
        onView(withId(id)).check(matches(withText(strNotDefault)))
    }
    @Test
    fun testNotClearFirstName() {
        val id=R.id.NU_FirstName
        onView(withId(id))
            .perform(replaceText(strNotDefault), closeSoftKeyboard())
        onView(withId(id)).check(matches(withText(strNotDefault)))
    }
    @Test
    fun testNotClearLastName() {
        val id=R.id.NU_LastName
        onView(withId(id))
            .perform(replaceText(strNotDefault), closeSoftKeyboard())
        onView(withId(id)).check(matches(withText(strNotDefault)))
    }

    // check No Default Values:


}