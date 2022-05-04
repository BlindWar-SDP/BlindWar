package ch.epfl.sdp.blindwar.game.solo

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBackUnconditionally
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.model.config.GameMode
import ch.epfl.sdp.blindwar.game.solo.util.typeSearchViewText
import ch.epfl.sdp.blindwar.game.util.DisplayableItemAdapter
import ch.epfl.sdp.blindwar.game.util.Tutorial
import junit.framework.Assert.assertEquals
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SoloActivityTest {
    @get:Rule
    var testRule = ActivityScenarioRule(
        SoloActivity::class.java
    )

    @get:Rule
    var permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.RECORD_AUDIO)

    @Test
    fun testBackButton() {
        onView(withId(R.id.back_button)).perform(click())
        //assertTrue(testRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    @Test
    fun testModeSelectionDisplayedOnLaunch() {
        onView(withId(R.id.regularButton_)).check(matches(isClickable()))
        onView(withId(R.id.survivalButton_)).check(matches(isClickable()))
        onView(withId(R.id.raceButton_)).check(matches(isClickable()))
        onView(withId(R.id.checkBox)).perform(click(), click())
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

            val expectedMode = when (btnId) {
                R.id.regularButton_ -> GameMode.REGULAR
                R.id.raceButton_ -> GameMode.TIMED
                else -> GameMode.SURVIVAL
            }
            assertEquals(expectedMode, observedMode)
        }
    }

    /** TODO: Debug Timeout
    @Test
    fun testLostThenWonGame() {
        searchPlaylist("The witcher", 2)

        // Click on the playlist
        onView(withId(R.id.playlistRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<DisplayableItemAdapter.DisplayableItemViewHolder>(
                    0,
                    click(),
                )
            )

        // Start game with the correct playlist
        onView(
            allOf(
                withId(R.id.startGame),
                withEffectiveVisibility(Visibility.VISIBLE)
            )
        ).perform(click())


        // Assert that the game is launched
        onView(withId(R.id.guessButton)).check(matches(isDisplayed()))


        // Create a function with a counter for the number of rounds
        simulateLostRound(false)


        onView(withId(R.id.replay)).perform(click())

        // Assert that the player is in the demo fragment
        onView(withId(R.id.audioVisualizer)).check(matches(isDisplayed()))

        guess(Tutorial.SONG_TESTING)

        // Assert that the player has guessed the song
        onView(withId(R.id.skip_next_summary)).check(matches(isDisplayed()))

        onView(withId(R.id.skip_next_summary)).perform(click())

        // Assert that the player has completed the game
        onView(withId(R.id.game_summary_fragment)).check(matches(isDisplayed()))

        onView(withId(R.id.quit)).perform(click())
    }**/

    private fun guess(guess: String) {
        onView(withId(R.id.guessEditText)).perform(typeText(guess))
        onView(withId(R.id.guessButton)).perform(click())
    }

    @Test
    fun testLostGameConnected() {
        testCompleteGame(0, Tutorial.ROUND)
        onView(withId(R.id.replay)).perform(click())
        onView(withId(R.id.audioVisualizer)).check(matches(isDisplayed()))
    }

    @Test
    fun testLostGameLocal() {
        closeSoftKeyboard()
        testCompleteGame(2, Tutorial.ROUND)
        onView(withId(R.id.quit)).perform(click())
    }

    private fun testCompleteGame(playlistIndex: Int, round: Int) {
        launchDemoWithMode(R.id.raceButton_, playlistIndex)
        onView(withId(R.id.guessButton)).check(matches(isDisplayed()))
        for (i in 0 until round) {
            simulateLostRound(false)
        }
    }

    private fun simulateLostRound(guess: Boolean) {
        onView(withId(R.id.microphone)).perform(click())
        onView(withId(R.id.startButton)).perform(click(), click())
        if (guess)
            guess("WRONG")

        val transitionDelay = 2000L
        Thread.sleep(Tutorial.TIME_TO_FIND.toLong() + transitionDelay)
        closeSoftKeyboard()
        pressBackUnconditionally()

    }

    private fun launchDemoWithMode(btnId: Int, position: Int) {
        launchPlaylistSelection(btnId, position, 3)

        onView(
            allOf(
                withId(R.id.startGame),
                withEffectiveVisibility(Visibility.VISIBLE)
            )
        ).perform(click())
    }

    @Test
    fun likeTest() {
        searchPlaylist("Fifa", 0)
        onView(
            allOf(
                withId(R.id.likeView),
                withEffectiveVisibility(Visibility.VISIBLE)
            )
        ).perform(click(), click())
            .check(matches(isClickable()))
    }


    private fun launchPlaylistSelection(btnId: Int, position: Int, chainClick: Int) {
        onView(withId(btnId)).perform(click())
        // Wait for network request completion
        Thread.sleep(2000)
        for (i in 0 until chainClick) {
            onView(withId(R.id.playlistRecyclerView))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<DisplayableItemAdapter.DisplayableItemViewHolder>(
                        position,
                        click(),
                    )
                )
        }
    }

    private fun searchPlaylist(search: String, position: Int) {
        launchPlaylistSelection(btnId = R.id.raceButton_, position = position, 1)
        onView(withId(R.id.searchBar)).perform(click())
        onView(withId(R.id.searchBar)).perform(typeSearchViewText(search))
        closeSoftKeyboard()
    }


    @Test
    fun testListenPreviewAfterSearch() {
        searchPlaylist("Fifa", 0)

        onView(allOf(withId(R.id.playPreview), withEffectiveVisibility(Visibility.VISIBLE)))
            .perform(click())

        onView(allOf(withId(R.id.playPreview), withEffectiveVisibility(Visibility.VISIBLE)))
            .perform(click())

        onView(allOf(withId(R.id.playPreview), withEffectiveVisibility(Visibility.VISIBLE)))
            .check(matches(isClickable()))
    }
}