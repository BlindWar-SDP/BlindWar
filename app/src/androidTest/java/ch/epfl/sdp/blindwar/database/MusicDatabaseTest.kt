package ch.epfl.sdp.blindwar.database

import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.ListResult
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MusicDatabaseTest : TestCase() {

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun cleanup() {
        Intents.release()
    }

    @Test
    fun testTaskNotNull() {
        assertNotNull(MusicDatabase.getSongReference())
    }

    @Test
    fun testSongReferenceNotNull() {
        var task: Task<ListResult>
        runBlocking {
            task = MusicDatabase.getSongReference()
        }

        assertFalse(task.result.items.isEmpty())
    }
}