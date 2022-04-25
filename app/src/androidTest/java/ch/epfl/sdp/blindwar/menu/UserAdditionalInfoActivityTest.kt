package ch.epfl.sdp.blindwar.menu

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.login.UserAdditionalInfoActivity
import ch.epfl.sdp.blindwar.login.UserNewInfoActivity
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions
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
    }

    @After
    fun cleanup() {
        Intents.release()
    }

    @Test
    fun testDescription() {
        typeTo(R.id.NUA_description,"My Description")
        closeSoftKeyboard()
        clickOn(R.id.NUA_Confirm_Btn)
        intended(hasComponent(UserNewInfoActivity::class.java.name))
    }

    @Test
    fun testDatePicker() {
        clickOn(R.id.NUA_select_birthdate)
        BaristaVisibilityAssertions.assertDisplayed(R.string.new_user_birthdatePicker)
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
}
