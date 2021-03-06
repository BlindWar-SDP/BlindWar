package ch.epfl.sdp.blindwar.game.solo.fragments

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.solo.fragments.SongSummaryFragment.Companion.ARTIST_KEY
import ch.epfl.sdp.blindwar.game.solo.fragments.SongSummaryFragment.Companion.COVER_KEY
import ch.epfl.sdp.blindwar.game.solo.fragments.SongSummaryFragment.Companion.SUCCESS_KEY
import ch.epfl.sdp.blindwar.game.solo.fragments.SongSummaryFragment.Companion.TITLE_KEY
import ch.epfl.sdp.blindwar.game.util.GameUtil.metadataTutorial
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
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
            val meta = metadataTutorial()["Daft Punk"]
            it.putString(ARTIST_KEY, meta?.author)
            it.putString(TITLE_KEY, meta?.name)
            it.putString(COVER_KEY, meta?.cover)
            it.putBoolean(SUCCESS_KEY, success)
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
        scenario.onFragment {
            assertFalse(it.liked())
        }

        onView(withId(R.id.likeView)).perform(click())
        scenario.onFragment {
            assertTrue(it.liked())
        }
    }


    @Test
    fun testSuccess() {
        val scenario = launchFragmentInContainer<SongSummaryFragment>(bundleSuccess)
        scenario.onFragment {
            assertTrue(it.success())
        }
    }
}