package ch.epfl.sdp.blindwar.game.solo.util

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers

/** Source :
 *  https://stackoverflow.com/questions/48037060/how-to-type-text-on-a-searchview-using-espresso
 */
fun typeSearchViewText(text: String): ViewAction {
    return object : ViewAction {
        override fun getDescription(): String {
            return "Search Query"
        }

        override fun getConstraints(): Matcher<View> {
            return Matchers.allOf(
                ViewMatchers.isDisplayed(),
                ViewMatchers.isAssignableFrom(SearchView::class.java)
            )
        }

        override fun perform(uiController: UiController?, view: View?) {
            (view as SearchView).setQuery(text, true)
        }
    }
}