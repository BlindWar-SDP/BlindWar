package ch.epfl.sdp.blindwar.game.util

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.profile.fragments.ProfileFragment
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainMusicTest : TestCase() {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun startTheMusic(){
        MainMusic.prepareAndPlay(context)
    }

    @Test
    fun startAndPauseTheMusic(){
        MainMusic.prepareAndPlay(context)
        MainMusic.pause()
    }

    @Test
    fun pauseUnpreparedMusic(){
        MainMusic.pause()
    }
}