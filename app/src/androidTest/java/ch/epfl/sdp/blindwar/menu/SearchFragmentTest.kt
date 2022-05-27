package ch.epfl.sdp.blindwar.menu

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