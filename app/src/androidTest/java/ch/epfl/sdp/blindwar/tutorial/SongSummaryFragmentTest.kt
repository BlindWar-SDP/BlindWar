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
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SongSummaryFragmentTest {

    private lateinit var bundleSuccess: Bundle
    private lateinit var bundleFailure: Bundle

    @Before
    fun setUp() {
        bundleSuccess = createBundle(success = true, liked = true)
        bundleFailure = createBundle(success = false, liked = false)
    }

    private fun createBundle(success: Boolean, liked: Boolean): Bundle {
        return Bundle().let {
            val meta = SONG_MAP["Daft Punk"]
            it.putString("artist", meta?.artist)
            it.putString("title", meta?.title)
            it.putString("image", meta?.imageUrl)
            it.putBoolean("success", success)
            it.putBoolean("liked", liked)
            it
        }
    }

    @Test
    fun testCorrectlySetLayout() {
        launchFragmentInContainer<SongSummaryFragment>(bundleFailure)
        onView((withId(R.id.artistTextView))).check(matches(withText("Daft Punk")))
        onView((withId(R.id.trackTextView))).check(matches(withText("One More Time")))
    }

    @Test
    fun testLikeAnimation() {
        val scenario = launchFragmentInContainer<SongSummaryFragment>(bundleSuccess)
        onView(withId(R.id.likeView)).perform(click())
        scenario.onFragment{
            assertEquals(false, it.liked())
        }

        onView(withId(R.id.likeView)).perform(click())
        scenario.onFragment{
            assertEquals(true, it.liked())
        }
    }

    @Test
    fun testSuccess() {
        val scenario = launchFragmentInContainer<SongSummaryFragment>(bundleSuccess)
        scenario.onFragment{
            assertEquals(true, it.success())
        }
    }

}