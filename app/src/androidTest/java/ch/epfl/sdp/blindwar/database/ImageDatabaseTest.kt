package ch.epfl.sdp.blindwar.database

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.profile.fragments.ProfileFragment
import junit.framework.TestCase
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ImageDatabaseTest : TestCase() {

    @Test
    fun testDownloadProfilePicture() {
        val fakeImagePath = "/gs:/blindwar-sdp.appspot.com/images/old_android.jpeg"
        launchFragmentInContainer<ProfileFragment>().onFragment {
           val reference = ImageDatabase.getImageReference(fakeImagePath)
            assertThat(reference.path, equalTo(fakeImagePath))
        }
    }
}