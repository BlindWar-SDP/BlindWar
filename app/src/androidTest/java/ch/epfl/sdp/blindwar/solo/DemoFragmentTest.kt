package ch.epfl.sdp.blindwar.solo

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.domain.game.SongMetaData
import ch.epfl.sdp.blindwar.ui.solo.DemoFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DemoFragmentTest {

    @Test
    fun testLaunchesDemo() {
        val scenario = launchFragmentInContainer<DemoFragment>()
        scenario.onFragment{
            it.onResume()
        }
    }

    @Test
    fun makeCorrectGuess() {
        val scenario = launchFragmentInContainer<DemoFragment>()
        var correctMetadata = SongMetaData("", "", "")
        scenario.onFragment {
            correctMetadata = it.game.currentMetadata()!!
        }

        //Log.d(TAG, correctMetadata.toString())
        onView(withId(R.id.guessEditText))
            .perform(
                clearText(),
                typeText(correctMetadata.title),
                closeSoftKeyboard()
            )

        onView(withId(R.id.guessButtonDemo)).perform(click())
    }
}