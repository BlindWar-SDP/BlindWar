package ch.epfl.sdp.blindwar.game.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NetworkConnectivityCheckerTest : TestCase() {
    @Test
    fun isOnline() {
        assertTrue(NetworkConnectivityChecker.isOnline())
    }
}