package ch.epfl.sdp.blindwar

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    /*@Test
    fun myClassMethod_ReturnsTrue() {
        activityRule.scenario.onActivity {
            onView(withId(R.id.mainButton))
        } // Optionally, access the activity.
    }*/

    @Test
    fun checkIntent() {
        Intents.init()
        onView(withId(R.id.mainButton)).perform(click())
        Intents.release()
    }
}