package ch.epfl.sdp.blindwar.database

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.game.model.OnlinePlaylist
import ch.epfl.sdp.blindwar.profile.fragments.ProfileFragment
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.android.gms.tasks.Tasks
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlaylistDatabaseTest : TestCase() {

    @Test
    fun testAddPlaylist() {
        launchFragmentInContainer<ProfileFragment>()
        val pid = "Test_Playlist"
        val testPlaylist = OnlinePlaylist(uid = pid)
        var playlist = OnlinePlaylist()


        runBlocking {
            withContext(Dispatchers.IO) {
                var playlist =
                    Tasks.await(PlaylistDatabase.addPlaylist(testPlaylist).continueWithTask{
                        PlaylistDatabase.getPlaylistReference(pid).get()
                    }).getValue(OnlinePlaylist::class.java)
            }
        }

        assertTrue(playlist.uid == testPlaylist.uid)
        PlaylistDatabase.removePlaylist(pid)
    }
}