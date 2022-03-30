package ch.epfl.sdp.blindwar.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.R
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DemoSRActivityTest : TestCase() {
    @get:Rule
    var testRule = ActivityScenarioRule(
        DemoSRActivity::class.java
    )

    @Test
    fun initWorks() {
        onView(withId(R.id.result_text_edit)).check(matches(withText("")))
    }
}