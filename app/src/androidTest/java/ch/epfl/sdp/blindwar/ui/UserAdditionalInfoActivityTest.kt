package ch.epfl.sdp.blindwar.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.R
import com.adevinta.android.barista.assertion.BaristaClickableAssertions.assertClickable
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaEditTextInteractions.typeTo
import com.adevinta.android.barista.interaction.BaristaSpinnerInteractions.clickSpinnerItem
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UserAdditionalInfoActivityTest : TestCase() {
    @get:Rule
    var testRule = ActivityScenarioRule(
        UserAdditionalInfoActivity::class.java
    )

    @Before
    fun setup() {
        Intents.init()
        closeSoftKeyboard()
    }

    @After
    fun cleanup() {
        Intents.release()
    }

    @Test
    fun testDescription() {
        typeTo(R.id.NUA_description, "My Description")
        closeSoftKeyboard()
        clickOn(R.id.NUA_Confirm_Btn)
        intended(hasComponent(UserNewInfoActivity::class.java.name))
    }

    @Test
    fun testDatePicker() {
        clickOn(R.id.NUA_select_birthdate)
        assertDisplayed(R.string.new_user_birthdatePicker)
        clickOn(android.R.string.ok)
        clickOn(R.id.NUA_Confirm_Btn)
        intended(hasComponent(UserNewInfoActivity::class.java.name))
    }

    @Test
    fun testSpinner() {
        clickSpinnerItem(R.id.gender_spinner, 3)
        clickOn(R.id.NUA_Confirm_Btn)
        intended(hasComponent(UserNewInfoActivity::class.java.name))
    }

    @Test
    fun testCancel() {
        clickOn(R.id.NUA_Cancel_Btn)
        intended(hasComponent(UserNewInfoActivity::class.java.name))
    }

    @Test
    fun testResetBirthdate() {
        closeSoftKeyboard()
        assertClickable(R.id.NUA_reset_birthdate)
    }

}
