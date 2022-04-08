package ch.epfl.sdp.blindwar.database

import android.widget.ImageView
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.ui.ProfileActivity
import junit.framework.TestCase
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ImageDatabaseTest : TestCase() {
    @get:Rule
    var testRule = ActivityScenarioRule(
        ProfileActivity::class.java
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
    fun testDowloadProfilePicture() {
        val fakeImagePath = "/gs:/blindwar-sdp.appspot.com/images/old_android.jpeg"
        var retImagePath = ""
        testRule.scenario.onActivity { activity ->
            val profilePictureView = activity.findViewById<ImageView>(R.id.profileImageView)
            retImagePath = ImageDatabase.downloadProfilePicture(
                fakeImagePath,
                profilePictureView, activity.applicationContext
            )
        }
        assertThat(retImagePath, equalTo(fakeImagePath))
    }
}