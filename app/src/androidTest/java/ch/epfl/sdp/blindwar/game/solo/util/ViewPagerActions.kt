package ch.epfl.sdp.blindwar.game.solo.util

/** Source :
 * https://github.com/android/views-widgets-samples/blob/master/ViewPager2/app/src/androidTest/java/androidx/viewpager2/integration/testapp/test/util/ViewPagerActions.kt
 */

import android.graphics.Rect
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import androidx.core.view.ViewCompat
import androidx.test.espresso.InjectEventSecurityException
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.viewpager2.widget.ViewPager2
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.Matcher


/**
 * ViewAction that issues a swipe gesture on a [ViewPager2] to move that ViewPager2 to the next
 * page, taking orientation and layout direction into account.
 */
fun swipeNext(): ViewAction {
    return SwipeAction(SwipeAction.Direction.FORWARD)
}

/**
 * ViewAction that issues a swipe gesture on a [ViewPager2] to move that ViewPager2 to the previous
 * page, taking orientation and layout direction into account.
 */
fun swipePrevious(): ViewAction {
    return SwipeAction(SwipeAction.Direction.BACKWARD)
}

private class SwipeAction(val direction: Direction) : ViewAction {
    enum class Direction {
        FORWARD,
        BACKWARD
    }

    override fun getDescription(): String = "Swiping $direction"

    override fun getConstraints(): Matcher<View> =
        allOf(
            anyOf(
                isAssignableFrom(ViewPager2::class.java),
                isDescendantOfA(isAssignableFrom(ViewPager2::class.java))
            ),
            isDisplayingAtLeast(90)
        )

    override fun perform(uiController: UiController, view: View) {
        val vp = if (view is ViewPager2) {
            view
        } else {
            var parent = view.parent
            while (parent !is ViewPager2 && parent != null) {
                parent = parent.parent
            }
            parent as ViewPager2
        }
        val isForward = direction == Direction.FORWARD
        val swipeAction: ViewAction = if (vp.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
            if (isForward == vp.isRtl()) swipeRight() else swipeLeft()
        } else {
            if (isForward) swipeUp() else swipeDown()
        }
        swipeAction.perform(uiController, view)
    }

    private fun ViewPager2.isRtl(): Boolean {
        return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL
    }
}

private class WaitForInjectMotionEventsAction : ViewAction {
    override fun getDescription(): String {
        return "Inject MotionEvents with UiController until the injection stops failing"
    }

    override fun getConstraints(): Matcher<View> {
        return ViewMatchers.isDisplayed()
    }

    override fun perform(uiController: UiController?, view: View?) {
        if (uiController == null || view == null) {
            throw PerformException.Builder()
                .withActionDescription("waiting for injection of MotionEvents")
                .withViewDescription("$view")
                .withCause(IllegalStateException("Missing UiController and/or View"))
                .build()
        }

        val rect = Rect()
        view.getGlobalVisibleRect(rect)
        val x = rect.exactCenterX()
        val y = rect.exactCenterY()

        var t: Long
        var event: MotionEvent
        var injectionSucceeded = false

        while (!injectionSucceeded) {
            t = SystemClock.uptimeMillis()
            event = MotionEvent.obtain(t, t, MotionEvent.ACTION_DOWN, x, y, 0)
            try {
                if (uiController.injectMotionEvent(event)) {
                    injectionSucceeded = true
                }
            } catch (e: InjectEventSecurityException) {
            }
            event.recycle()

            if (injectionSucceeded) {
                event = MotionEvent.obtain(t, t + 10, MotionEvent.ACTION_CANCEL, x, y, 0)
                uiController.injectMotionEvent(event)
                event.recycle()
            } else {
                uiController.loopMainThreadForAtLeast(10)
            }
        }
    }
}