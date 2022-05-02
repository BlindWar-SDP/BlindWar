package ch.epfl.sdp.blindwar.game.util

import android.os.Bundle
import android.speech.SpeechRecognizer
import android.widget.EditText
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.game.util.VoiceRecognizer
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import kotlin.collections.ArrayList

@RunWith(AndroidJUnit4::class)
class VoiceRecognizerTest : TestCase() {

    private val voiceRecognizer = VoiceRecognizer()

    @Test
    fun placeholder() {
        assert(true)
    }


    @Test
    fun init() {
        testRule.scenario.onActivity { activity ->
            voiceRecognizer.resultsRecognized = "no"
            voiceRecognizer.init(activity, EditText(activity), Locale.ENGLISH.toLanguageTag())
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

    @Test
    fun start() {
        testRule.scenario.onActivity { activity ->
            val tv = EditText(activity)
            voiceRecognizer.init(activity, tv, Locale.ENGLISH.toLanguageTag())
            voiceRecognizer.resultsRecognized = "no"
            voiceRecognizer.start()
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

    @Test
    fun stop() {
        testRule.scenario.onActivity { activity ->
            val tv = EditText(activity)
            voiceRecognizer.init(activity, tv, Locale.ENGLISH.toLanguageTag())
            voiceRecognizer.resultsRecognized = "no"
            voiceRecognizer.start()
            voiceRecognizer.stop()
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

    @Test
    fun destroy() {
        testRule.scenario.onActivity { activity ->
            val tv = EditText(activity)
            voiceRecognizer.init(activity, tv, Locale.ENGLISH.toLanguageTag())
            voiceRecognizer.resultsRecognized = "no"
            voiceRecognizer.destroy()
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

    @Test
    fun onReadyForSpeech() {
        testRule.scenario.onActivity { activity ->
            val tv = EditText(activity)
            voiceRecognizer.init(activity, tv, Locale.ENGLISH.toLanguageTag())
            voiceRecognizer.onReadyForSpeech(null)
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

    @Test
    fun onBeginningOfSpeech() {
        testRule.scenario.onActivity { activity ->
            val tv = EditText(activity)
            voiceRecognizer.init(activity, tv, Locale.ENGLISH.toLanguageTag())
            voiceRecognizer.onBeginningOfSpeech()
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

    @Test
    fun onRmsChanged() {
        testRule.scenario.onActivity { activity ->
            val tv = EditText(activity)
            voiceRecognizer.init(activity, tv, Locale.ENGLISH.toLanguageTag())
            voiceRecognizer.onRmsChanged(0f)
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

    @Test
    fun onBufferReceived() {
        testRule.scenario.onActivity { activity ->
            val tv = EditText(activity)
            voiceRecognizer.init(activity, tv, Locale.ENGLISH.toLanguageTag())
            voiceRecognizer.onBufferReceived(null)
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

    @Test
    fun onEndOfSpeech() {
        testRule.scenario.onActivity { activity ->
            val tv = EditText(activity)
            voiceRecognizer.init(activity, tv, Locale.ENGLISH.toLanguageTag())
            voiceRecognizer.onEndOfSpeech()
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

    @Test
    fun onError() {
        testRule.scenario.onActivity { activity ->
            val tv = EditText(activity)
            voiceRecognizer.init(activity, tv, Locale.ENGLISH.toLanguageTag())
            voiceRecognizer.onError(0)
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

    @Test
    fun onResults() {
        testRule.scenario.onActivity { activity ->
            val tv = EditText(activity)
            voiceRecognizer.init(activity, tv, Locale.ENGLISH.toLanguageTag())
            val bundle = Bundle()
            val list = ArrayList<String>()
            list.add("yo")
            bundle.putStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION, list)
            voiceRecognizer.onResults(bundle)
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "yo")
        }
    }

    @Test
    fun onPartialResults() {
        testRule.scenario.onActivity { activity ->
            val tv = EditText(activity)
            voiceRecognizer.init(activity, tv, Locale.ENGLISH.toLanguageTag())
            val bundle = Bundle()
            val list = ArrayList<String>()
            list.add("yo")
            bundle.putStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION, list)
            voiceRecognizer.onPartialResults(bundle)
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

    @Test
    fun onEvent() {
        testRule.scenario.onActivity { activity ->
            val tv = EditText(activity)
            voiceRecognizer.init(activity, tv, Locale.ENGLISH.toLanguageTag())
            voiceRecognizer.onEvent(0, null)
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

}