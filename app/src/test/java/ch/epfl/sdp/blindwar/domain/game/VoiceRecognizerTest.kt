package ch.epfl.sdp.blindwar.domain.game

import junit.framework.TestCase
import org.junit.Test

class VoiceRecognizerTest : TestCase() {

    private val voiceRecognizer: VoiceRecognizer = VoiceRecognizer()

    @Test
    fun start() {
        assert(voiceRecognizer.resultsRecognized == "")
    }
}