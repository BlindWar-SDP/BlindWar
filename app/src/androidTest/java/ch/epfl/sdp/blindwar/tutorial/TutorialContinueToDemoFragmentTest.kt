package ch.epfl.sdp.blindwar.tutorial

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.ui.tutorial.DemoActivity
import ch.epfl.sdp.blindwar.ui.tutorial.TutorialContinueToDemoFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TutorialContinueToDemoFragmentTest {

    @Test
    fun testLaunchesDemo() {
        launchFragmentInContainer<TutorialContinueToDemoFragment>()
        init()
        onView(withId(R.id.continueDemoButton)).perform(click())
        intended(hasComponent(DemoActivity::class.java.name))
    }
}