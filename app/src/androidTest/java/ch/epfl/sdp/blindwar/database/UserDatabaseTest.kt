package ch.epfl.sdp.blindwar.database

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.profile.fragments.ProfileFragment
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UserDatabaseTest : TestCase() {

    private val testUID = "JOJO"

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun cleanup() {
        Intents.release()
    }

    @Test
    fun placeholder() {
        assert(true)
    }

    @Test
    fun setEloCorrectly() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            UserDatabase.setElo(testUID, 1000)
            // should check elo with future
        }

    }

    @Test
    fun addProfilePictureCorrectly() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            UserDatabase.addProfilePicture(testUID, "")
            // should check path with futures
        }
    }

    @Test
    fun getImageReferenceCorrectly() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            assertNotNull(UserDatabase.getImageReference(testUID))
        }

    }

    @Test
    fun getEloReferenceCorrectly() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            assertNotNull(UserDatabase.getEloReference(testUID))
        }
    }
}