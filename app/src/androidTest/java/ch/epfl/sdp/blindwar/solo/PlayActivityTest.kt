package ch.epfl.sdp.blindwar.solo

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBackUnconditionally
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.domain.game.GameMode
import ch.epfl.sdp.blindwar.domain.game.Tutorial
import ch.epfl.sdp.blindwar.ui.solo.PlayActivity
import ch.epfl.sdp.blindwar.ui.solo.PlaylistAdapter
import junit.framework.Assert.assertEquals
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayActivityTest {
    @get:Rule
    var testRule = ActivityScenarioRule(
        PlayActivity::class.java
    )

    @Test
    fun testModeSelectionDisplayedOnLaunch() {
        onView(withId(R.id.regularButton_)).check(matches(isClickable()))
        onView(withId(R.id.survivalButton_)).check(matches(isClickable()))
        onView(withId(R.id.raceButton_)).check(matches(isClickable()))
    }

    @Test
    fun testRegularMode() {
        testLaunchMode(R.id.regularButton_)
    }

    @Test
    fun testSurvivalMode() {
        testLaunchMode(R.id.survivalButton_)
    }

    @Test
    fun testRaceMode() {
        testLaunchMode(R.id.raceButton_)
    }

    private fun testLaunchMode(btnId: Int) {
        onView(withId(btnId)).perform(click())
        testRule.scenario.onActivity {
            val observedMode = it.gameInstanceViewModel
                .gameInstance
                .value
                ?.gameConfig?.mode

            val expectedMode = when(btnId) {
                R.id.regularButton_ -> GameMode.REGULAR
                R.id.raceButton_ -> GameMode.TIMED
                else -> GameMode.SURVIVAL
            }
            assertEquals(expectedMode, observedMode)
        }
    }

    @Test
    fun testLostGameConnected() {
        testCompleteGame(0)
    }

    @Test
    fun testLostGameLocal() {
        testCompleteGame(1)
    }

    private fun testCompleteGame(playlistIndex: Int) {
        launchDemoWithMode(R.id.regularButton_, playlistIndex)
        onView(withId(R.id.guessButtonDemo)).check(matches(isDisplayed()))
        for (i in 0 until 3) {
            simulateLostRound()
        }

        onView(withId(R.id.quit)).perform(click())
    }

    private fun simulateLostRound() {
        val transitionDelay = 2000L
        Thread.sleep(Tutorial.TIME_TO_FIND.toLong() + transitionDelay)
        pressBackUnconditionally()
    }

    private fun launchDemoWithMode(btnId: Int, position: Int) {
        launchPlaylistSelection(btnId, position, 3)

        onView(allOf(withId(R.id.startGame),
            withEffectiveVisibility(Visibility.VISIBLE))).perform(click())
    }


    private fun launchPlaylistSelection(btnId: Int, position: Int, chainClick: Int) {
        onView(withId(btnId)).perform(click())
        for (i in 0 until chainClick) {
            onView(withId(R.id.playlistRecyclerView))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<PlaylistAdapter.PlaylistViewHolder>(
                        position,
                        click(),
                    )
                )
        }
    }


    @Test
    fun testListenPreviewAfterSearch() {
        launchPlaylistSelection(btnId = R.id.raceButton_, position = 0, 1)
        onView(withId(R.id.searchBar)).perform(click())
        onView(withId(R.id.searchBar)).perform(typeSearchViewText("Fifa"))

        onView(allOf(withId(R.id.playPreview), withEffectiveVisibility(Visibility.VISIBLE)))
            .perform(click())

        onView(allOf(withId(R.id.playPreview), withEffectiveVisibility(Visibility.VISIBLE)))
            .perform(click())

        onView(allOf(withId(R.id.playPreview), withEffectiveVisibility(Visibility.VISIBLE)))
            .check(matches(isClickable()))
    }

    /** Source :
     *  https://stackoverflow.com/questions/48037060/how-to-type-text-on-a-searchview-using-espresso
     */
    private fun typeSearchViewText(text: String): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Search Query"
            }

            override fun getConstraints(): Matcher<View> {
                return allOf(isDisplayed(), isAssignableFrom(SearchView::class.java))
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as SearchView).setQuery(text, true)

            }
        }
    }
}