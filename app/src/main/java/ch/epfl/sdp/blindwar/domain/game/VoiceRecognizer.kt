package ch.epfl.sdp.blindwar.domain.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.TextView
import java.util.*

/**
 * Class used to recognize voice
 *
 * could be singleton if doesn't have a textView in it
 *
 */
class VoiceRecognizer : RecognitionListener {
    private var speechRecognizer: SpeechRecognizer? = null
    var resultsRecognized: String = ""
    private var speechRecognizerIntent: Intent? = null
    private var textViewResult: TextView? = null
    override fun onReadyForSpeech(params: Bundle?) {}

    override fun onBeginningOfSpeech() {}

    override fun onRmsChanged(rmsdB: Float) {}

    override fun onBufferReceived(buffer: ByteArray?) {}

    override fun onEndOfSpeech() {}

    override fun onError(error: Int) {}

    /**
     * Function used when the SpeechRecognizer recognize something
     *
     * @param results
     */
    override fun onResults(results: Bundle) {
        val data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        resultsRecognized = data!![0]
        textViewResult?.text = resultsRecognized
    }

    /**
     * Function used when the SpeechRecognizer recognize partially something
     *
     * @param
     */
    override fun onPartialResults(partialResults: Bundle?) {
        val data = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        resultsRecognized = data!![0]
        textViewResult?.text = resultsRecognized
    }

    override fun onEvent(eventType: Int, params: Bundle?) {}

    /**
     * Initialize every attributes of the class
     *
     * @param context
     * @param textView
     */
    fun init(context: Context, textView: TextView) {
        if (speechRecognizer == null && speechRecognizerIntent == null) {
            textViewResult = textView
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

            speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            speechRecognizerIntent!!.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            resultsRecognized = ""
            speechRecognizerIntent!!.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            (speechRecognizer as SpeechRecognizer).setRecognitionListener(this)
        }
    }

    /**
     * Start to listen
     *
     */
    fun start() {
        resultsRecognized = ""
        speechRecognizer!!.startListening(speechRecognizerIntent)
    }

    /**
     * Stop listening
     *
     */
    fun stop() {
        speechRecognizer!!.stopListening()
    }

    /**
     * Destroy speech recognizer
     *
     */
    fun destroy() {
        speechRecognizer?.destroy()
        speechRecognizer = null
        speechRecognizerIntent = null
        resultsRecognized = ""
    }
}