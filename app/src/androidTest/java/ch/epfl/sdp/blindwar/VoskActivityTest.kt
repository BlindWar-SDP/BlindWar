package ch.epfl.sdp.blindwar

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import junit.framework.TestCase
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test

class VoskActivityTest : TestCase() {
    @get:Rule
    var permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.RECORD_AUDIO)

    @get:Rule
    var testRule = ActivityScenarioRule(
        VoskActivity::class.java
    )

    @Test
    fun onCreate() {
        onView(withId(R.id.result_text)).check(matches(isEnabled()))
    }

    @Test
    fun onResult() {
        onView(withId(R.id.result_text)).check(matches(isEnabled()))
    }

    @Test
    fun onFinalResult() {
        onView(withId(R.id.result_text)).check(matches(isEnabled()))
    }

    @Test
    fun onPartialResult() {
        onView(withId(R.id.result_text)).check(matches(isEnabled()))
    }

    @Test
    fun onError() {
        onView(withId(R.id.recognize_mic)).check(matches(not(isEnabled())))
    }

    @Test
    fun onTimeout() {
        onView(withId(R.id.recognize_mic)).check(matches(isEnabled()))
    }
}