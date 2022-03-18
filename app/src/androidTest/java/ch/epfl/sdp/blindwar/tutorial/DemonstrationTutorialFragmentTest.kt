package ch.epfl.sdp.blindwar.tutorial

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.domain.game.SongImageUrlConstants
import ch.epfl.sdp.blindwar.domain.game.SongSummaryFragment
import ch.epfl.sdp.blindwar.ui.tutorial.DemonstrationTutorialFragment
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DemonstrationTutorialFragmentTest {

    /** TODO: verify the API requests with a mock server **/
    @Test
    fun testCorrectlySetLayout() = runTest {
        launchFragmentInContainer<DemonstrationTutorialFragment>()
        onView(withId(R.id.title)).check(matches(withText("DEMO")))
    }
}