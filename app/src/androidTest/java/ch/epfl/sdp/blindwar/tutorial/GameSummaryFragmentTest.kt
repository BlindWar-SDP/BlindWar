package ch.epfl.sdp.blindwar.tutorial

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.ui.tutorial.GameSummaryFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameSummaryFragmentTest {

    @Test
    fun testCorrectlySetLayout() {
        launchFragmentInContainer<GameSummaryFragment>()
        onView(ViewMatchers.withId(R.id.title))
            .check(ViewAssertions.matches(ViewMatchers.withText("GAME")))
    }
}