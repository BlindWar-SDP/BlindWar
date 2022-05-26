package ch.epfl.sdp.blindwar.database

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.profile.fragments.ProfileFragment
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MusicDatabaseTest : TestCase() {

    @Test
    fun testTaskNotNull() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            assertNotNull(MusicDatabase.getSongReference())
        }
    }

    @Test
    fun testSongReferenceNotNull() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            val task = MusicDatabase.getSongReference()
            task.addOnSuccessListener {
                assertFalse(task.result.items.isEmpty())
            }
        }
    }
}