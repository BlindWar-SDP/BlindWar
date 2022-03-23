package ch.epfl.sdp.blindwar.domain.game

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.test.core.app.ApplicationProvider.getApplicationContext


object VoiceRecognizer : RecognitionListener {
    private val recognizer = this
    private lateinit var sr: SpeechRecognizer

    override fun onReadyForSpeech(params: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onBeginningOfSpeech() {
        TODO("Not yet implemented")
    }

    override fun onRmsChanged(rmsdB: Float) {
        TODO("Not yet implemented")
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        TODO("Not yet implemented")
    }

    override fun onEndOfSpeech() {
        TODO("Not yet implemented")
    }

    override fun onError(error: Int) {
        TODO("Not yet implemented")
    }

    override fun onResults(results: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onPartialResults(partialResults: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        TODO("Not yet implemented")
    }

    fun startRecognize() {
        sr = SpeechRecognizer.createSpeechRecognizer(getApplicationContext())
        sr.setRecognitionListener(recognizer)
        sr.startListening(RecognizerIntent.getVoiceDetailsIntent(getApplicationContext()))
    }

    fun stopRecognize() {
        sr.stopListening()
    }
}