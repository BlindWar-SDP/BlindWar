package ch.epfl.sdp.blindwar.database

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.profile.fragments.ProfileFragment
import ch.epfl.sdp.blindwar.profile.model.User
import junit.framework.TestCase
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UserDatabaseTest : TestCase() {
    private val testUID = "JOJO"

    @Test
    fun setEloCorrectly() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            val elo = 1000
            UserDatabase.setElo(testUID, elo)
            UserDatabase.userReference.child(testUID).get().addOnSuccessListener {
                assertTrue((it.getValue(User::class.java)?.userStatistics?.elo == elo))
            }
        }
    }

    @Test
    fun addProfilePictureCorrectly() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            val path = "TEST_PATH"
            UserDatabase.addProfilePicture(testUID, path)
            UserDatabase.userReference.child(testUID).get().addOnSuccessListener {
                assertTrue((it.getValue(User::class.java)?.profilePicture == path))
            }
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