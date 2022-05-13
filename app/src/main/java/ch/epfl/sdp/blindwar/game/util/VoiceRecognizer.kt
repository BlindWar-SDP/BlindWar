package ch.epfl.sdp.blindwar.game.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.lifecycle.MutableLiveData

/**
 * Class used to recognize voice
 *
 * could be singleton if doesn't have a textView in it
 */
class VoiceRecognizer : RecognitionListener {
    private var speechRecognizer: SpeechRecognizer? = null
    var resultsRecognized: String = ""
    private var speechRecognizerIntent: Intent? = null
    val resultString = MutableLiveData("")

    /**
     * Function used when the SpeechRecognizer recognize something
     *
     * @param results
     */
    override fun onResults(results: Bundle) {
        val data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (data != null && data[0] != null) {
            resultsRecognized = data[0]
            resultString.postValue(resultsRecognized)
        }
    }

    /**
     * Initialize every attributes of the class
     *
     * @param context
     * @param language
     */
    fun init(context: Context, language: String) {
        if (speechRecognizer == null && speechRecognizerIntent == null) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

            speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            speechRecognizerIntent!!.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            speechRecognizerIntent!!.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                language
            )
            speechRecognizerIntent!!.putExtra(
                "android.speech.extra.MASK_OFFENSIVE_WORDS",
                false
            )

            /**
            speechRecognizerIntent!!.putExtra(
            RecognizerIntent.EXTRA_CALLING_PACKAGE, context.applicationInfo.packageName
            )**/

            //only for android 13
            resultsRecognized = ""
            (speechRecognizer as SpeechRecognizer).setRecognitionListener(this)
        }
    }

    /**
     * Start to listen
     */
    fun start() {
        resultsRecognized = ""
        speechRecognizer!!.startListening(speechRecognizerIntent)
    }

    /**
     * Stop listening
     */
    fun stop() {
        speechRecognizer!!.stopListening()
    }

    /**
     * Destroy speech recognizer
     */
    fun destroy() {
        speechRecognizer?.destroy()
        speechRecognizer = null
        speechRecognizerIntent = null
        resultsRecognized = ""
    }

    // UNUSED OVERRIDDEN FUNCTIONS
    /**
     * Function used when the SpeechRecognizer recognize partially something
     *
     * @param partialResults
     */
    override fun onPartialResults(partialResults: Bundle?) {}
    override fun onEvent(eventType: Int, params: Bundle?) {}
    override fun onReadyForSpeech(params: Bundle?) {}
    override fun onBeginningOfSpeech() {}
    override fun onRmsChanged(rmsdB: Float) {}
    override fun onBufferReceived(buffer: ByteArray?) {}
    override fun onEndOfSpeech() {}
    override fun onError(error: Int) {}
}