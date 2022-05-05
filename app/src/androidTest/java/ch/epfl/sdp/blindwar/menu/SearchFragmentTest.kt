package ch.epfl.sdp.blindwar.menu

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.solo.util.typeSearchViewText
import org.junit.Test
import org.junit.runner.RunWith
/**
@RunWith(AndroidJUnit4::class)
class SearchFragmentTest {

    /
    @Test
    fun testSubmitSearch() {
        launchFragmentInContainer<SearchFragment>()
        onView(withId(R.id.searchBar)).perform(click())
        onView(withId(R.id.searchBar)).perform(typeSearchViewText("cirrus"))
        // Assert that the first search result
        onView(withText("SEPTEMBER")).check(matches(isDisplayed()))
    }
}**/