package ch.epfl.sdp.blindwar.game.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith

/**
@RunWith(AndroidJUnit4::class)
class QRCodeGeneratorTest: TestCase() {


    @Test
    fun testEmptyUrl() {
        assertThrows(RuntimeException::class.java) { QRCodeGenerator.encodeUrl("") }
    }

    @Test
    fun testBasicUrl() {
        val bitmap = QRCodeGenerator.encodeUrl("https://github.com/BlindWar-SDP/BlindWar/tree/main")
        assertNotNull(bitmap)
        // TODO: Test the actual result of the voice recognizer
    }
} **/