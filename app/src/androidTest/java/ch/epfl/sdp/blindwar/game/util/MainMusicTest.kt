package ch.epfl.sdp.blindwar.game.util

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainMusicTest : TestCase() {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setup() {
        MainMusic.reset()
    }

    @Test(expected = Test.None::class)
    fun playAndPauseMusicTheMusic(){
        // Test now exception is thrown
        MainMusic.prepareAndPlay(context)
        MainMusic.pause()
    }

    @Test
    fun resetAndPause(){
        MainMusic.prepareAndPlay(context)
        MainMusic.reset()
        assertThrows(IllegalStateException::class.java) {
            MainMusic.pause()
        }
    }

    @Test
    fun playUnpreparedMusic(){
        assertThrows(IllegalStateException::class.java) {
            MainMusic.play()
        }
    }

    @Test
    fun pauseUnpreparedMusic(){
        assertThrows(IllegalStateException::class.java) {
            MainMusic.pause()
        }
    }
}