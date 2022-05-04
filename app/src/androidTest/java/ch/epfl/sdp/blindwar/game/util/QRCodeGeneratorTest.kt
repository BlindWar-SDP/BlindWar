package ch.epfl.sdp.blindwar.game.util

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.profile.fragments.ProfileFragment
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class QRCodeGeneratorTest: TestCase() {


    @Test
    fun testEmptyUrl() {
        assertNotNull(QRCodeGenerator.encodeUrl(""))
    }

    @Test
    fun testBasicUrl() {
        launchFragmentInContainer<ProfileFragment>()
        val bitmap = QRCodeGenerator.encodeUrl("https://github.com/BlindWar-SDP/BlindWar/tree/main")
        assertNotNull(bitmap)
    }
}