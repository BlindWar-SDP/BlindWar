package ch.epfl.sdp.blindwar.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import ch.epfl.sdp.blindwar.R
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DemoSRActivityTest : TestCase() {

    @get:Rule
    var permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.RECORD_AUDIO)

    @get:Rule
    var testRule = ActivityScenarioRule(
        DemoSRActivity::class.java
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
    fun initializationWorks() {
        onView(withId(R.id.result_text_edit_sr)).check(matches(withText("")))
    }

    @Test
    fun testButtonEnglish() {
        onView(withId(R.id.recognize_mic_en)).perform(click())
        onView(withId(R.id.result_text_edit_sr)).check(matches(withText("")))
    }

    @Test
    fun testButtonFrench() {
        onView(withId(R.id.recognize_mic_fr)).perform(click())
        onView(withId(R.id.result_text_edit_sr)).check(matches(withText("")))
    }
}