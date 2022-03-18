package ch.epfl.sdp.blindwar.tutorial

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.viewpager2.widget.ViewPager2
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.tutorial.util.*
import ch.epfl.sdp.blindwar.ui.tutorial.TutorialActivity
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TutorialActivityTest {
    @get:Rule
    var testRule = ActivityScenarioRule(TutorialActivity::class.java)

    private lateinit var idleWatcher: ViewPagerIdleWatcher
    private lateinit var viewPager: ViewPager2

    /** Source :
     * https://github.com/android/views-widgets-samples/blob/master/ViewPager2/app/src/androidTest/java/androidx/viewpager2/integration/testapp/test/BaseTest.kt
     */
    @Before
    fun setUp() {
        val layoutId = R.layout.activity_tutorial
        testRule.scenario.onActivity {
            viewPager = it.findViewById(R.id.pager)
        }

        idleWatcher = ViewPagerIdleWatcher(viewPager)
    }

    @After
    fun tearDown() {
        idleWatcher.unregister()
    }

    @Test
    fun testSwipe() {
        // Swipe to page 2
        swipeToNextPage()
        verifyCurrentPage("Tutorial")

        // Swipe back to page 1
        swipeToPreviousPage()
        verifyCurrentPage("Tutorial")
    }

    private fun swipeToNextPage() {
        onViewPager().perform(swipeNext())
        idleWatcher.waitForIdle()
        onIdle()
    }

    private fun swipeToPreviousPage() {
        onViewPager().perform(swipePrevious())
        idleWatcher.waitForIdle()
        onIdle()
    }

    private fun verifyCurrentPage(pageText: String) {
        verifyCurrentPage(hasDescendant(withText(pageText)))
    }

    private fun verifyCurrentPage(matcher: Matcher<View>) {
        onCurrentPage().check(matches(matcher))
    }
}