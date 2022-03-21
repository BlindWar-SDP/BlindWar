package ch.epfl.sdp.blindwar.tutorial

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.domain.game.SongImageUrlConstants.SONG_MAP
import ch.epfl.sdp.blindwar.ui.tutorial.SongSummaryFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SongSummaryFragmentTest {

    @Test
    fun testCorrectlySetLayout() {
        val bundle = Bundle().let {
            it.putString("Artist", "Daft Punk")
            it.putString("Title", "One More Time")
            it.putString("Image", SONG_MAP["Daft Punk"])
            it
        }

        launchFragmentInContainer<SongSummaryFragment>(bundle)
        onView((withId(R.id.artistTextView))).check(matches(withText("Daft Punk")))
        onView((withId(R.id.trackTextView))).check(matches(withText("One More Time")))
    }

    @Test
    fun testLikeAnimation() {
        val bundle = Bundle().let {
            it.putString("Artist", "Daft Punk")
            it.putString("Title", "One More Time")
            it.putString("Image", SONG_MAP["Daft Punk"])
            it
        }

        val scenario = launchFragmentInContainer<SongSummaryFragment>(bundle)
        onView(withId(R.id.likeView)).perform(click()).check(matches(isDisplayed()))
    }
}