package ch.epfl.sdp.blindwar.tutorial

import android.view.View
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.viewpager2.widget.ViewPager2
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.tutorial.util.*
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
class TutorialActivityTest {
    @get:Rule
    var testRule = ActivityScenarioRule(TutorialActivity::class.java)

    lateinit var idleWatcher: ViewPagerIdleWatcher
    lateinit var viewPager: ViewPager2

    /** Source :
     * https://github.com/android/views-widgets-samples/blob/master/ViewPager2/app/src/androidTest/java/androidx/viewpager2/integration/testapp/test/BaseTest.kt
     */
    @Before
    fun setUp() {
        val layoutId = R.layout.activity_tutorial
        testRule.scenario.onActivity {
            viewPager = it.findViewById<ViewPager2>(R.id.pager)
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

    fun swipeToNextPage() {
        onViewPager().perform(swipeNext())
        idleWatcher.waitForIdle()
        onIdle()
    }

    fun swipeToPreviousPage() {
        onViewPager().perform(swipePrevious())
        idleWatcher.waitForIdle()
        onIdle()
    }

    fun verifyCurrentPage(pageText: String) {
        verifyCurrentPage(hasDescendant(withText(pageText)))
    }

    fun verifyCurrentPage(matcher: Matcher<View>) {
        onCurrentPage().check(matches(matcher))
    }
}