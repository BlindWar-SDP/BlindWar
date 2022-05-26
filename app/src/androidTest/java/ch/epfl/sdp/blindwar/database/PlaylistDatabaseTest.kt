package ch.epfl.sdp.blindwar.database

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.game.model.OnlinePlaylist
import ch.epfl.sdp.blindwar.profile.fragments.ProfileFragment
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlaylistDatabaseTest : TestCase() {

    @Test
    fun testAddAndRemovePlaylist() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            val pid = "Test_Playlist"
            val playlist = OnlinePlaylist(uid = pid)
            PlaylistDatabase.addPlaylist(playlist)
            PlaylistDatabase.getPlaylistReference(pid).get().addOnSuccessListener {
                assertTrue(it.getValue(OnlinePlaylist::class.java)?.uid == pid)
            }

            PlaylistDatabase.removePlaylist(pid)
        }
    }
}