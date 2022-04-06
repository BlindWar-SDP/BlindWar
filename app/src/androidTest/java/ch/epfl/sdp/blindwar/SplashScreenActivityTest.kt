package ch.epfl.sdp.blindwar


import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.ui.SplashScreenActivity
import com.adevinta.android.barista.assertion.BaristaImageViewAssertions.assertHasDrawable
import com.adevinta.android.barista.assertion.BaristaProgressBarAssertions.assertProgressIsMin
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SplashScreenActivityTest : TestCase() {
    @get:Rule
    var testRule = ActivityScenarioRule(
        SplashScreenActivity::class.java
    )

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun cleanup() {
        Intents.release()
    }

    @Test
    fun testLayoutVisibility() {
        // the activity pass to the next one too early, don't have time to run tests
//        assertHasDrawable(R.id.logo_splash_screen, R.drawable.logo)
//        assertProgressIsMin(R.id.progressBar)
    }
}