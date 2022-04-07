package ch.epfl.sdp.blindwar

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.ui.*
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ProfileActivityTest : TestCase() {
    @get:Rule
    var testRule = ActivityScenarioRule(
        ProfileActivity::class.java
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
    fun testStatisticsButton() {
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.statsButton))
            .perform(click())
        intended(hasComponent(StatisticsActivity::class.java.name))
    }

    @Test
    fun testDeleteButton_cancel() {
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.deleteProfile))
            .perform(click())
        BaristaVisibilityAssertions.assertDisplayed(R.string.account_deletion_text)
        clickOn(android.R.string.cancel)
    }

    @Test
    fun testDeleteButton_ok_cancel() {
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.deleteProfile))
            .perform(click())
        BaristaVisibilityAssertions.assertDisplayed(R.string.account_deletion_text)
        clickOn(android.R.string.ok)
        BaristaVisibilityAssertions.assertDisplayed(R.string.account_deletion_confirm_text)
        clickOn(android.R.string.cancel)
    }

    @Test
    fun testDeleteButton_ok_ok() {
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.deleteProfile))
            .perform(click())
        BaristaVisibilityAssertions.assertDisplayed(R.string.account_deletion_text)
        clickOn(android.R.string.ok)
        BaristaVisibilityAssertions.assertDisplayed(R.string.account_deletion_confirm_text)
        clickOn(android.R.string.ok)
//        BaristaVisibilityAssertions.assertDisplayed(R.string.deletion_success) // toast not detected
        intended(hasComponent(SplashScreenActivity::class.java.name))
    }

    @Test
    fun testLogoutButton() {
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.logoutButton))
            .perform(click())
        intended(hasComponent(SplashScreenActivity::class.java.name))
    }

    @Test
    fun testEditProfileButton() {
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.editProfileButton))
            .perform(click())
        intended(hasComponent(UserNewInfoActivity::class.java.name))
    }

}