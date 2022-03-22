package ch.epfl.sdp.blindwar

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
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
    fun testConfirmButton() {
        onView(withId(R.id.NU_Confirm_Btn))
            .perform(ViewActions.click())
            intended(IntentMatchers.hasComponent(MainMenuActivity::class.java.name))
    }
}