package ch.epfl.sdp.blindwar.database

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.menu.SearchFragment
import ch.epfl.sdp.blindwar.profile.fragments.ProfileFragment
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.ListResult
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait
import org.junit.After
import org.junit.Before
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