package ch.epfl.sdp.blindwar.tutorial

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TutorialContinueToDemoFragment {

    @get:Rule
    var testRule = ActivityScenarioRule(
        TutorialActivity::class.java
    )

    @Test
    fun startTheDemo() {
        Espresso.onView(ViewMatchers.withId(R.id.continueDemoButton))
            .perform(ViewActions.click())
        intended(hasComponent(DemoActivity::class.java.name))
    }
}