package ch.epfl.sdp.blindwar.domain.game

import android.os.Bundle
import android.speech.SpeechRecognizer
import android.widget.TextView
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.ui.VoskActivity
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VoiceRecognizerTest : TestCase() {

    private val voiceRecognizer = VoiceRecognizer()

    @get:Rule
    var testRule = ActivityScenarioRule(
        VoskActivity::class.java
    )

    @Test
    fun init() {
        testRule.scenario.onActivity { activity ->
            voiceRecognizer.resultsRecognized = "no"
            voiceRecognizer.init(activity, TextView(activity))
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

    @Test
    fun start() {
        testRule.scenario.onActivity { activity ->
            val tv = TextView(activity)
            voiceRecognizer.init(activity, tv)
            voiceRecognizer.resultsRecognized = "no"
            voiceRecognizer.start()
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

    @Test
    fun stop() {
        testRule.scenario.onActivity { activity ->
            val tv = TextView(activity)
            voiceRecognizer.init(activity, tv)
            voiceRecognizer.resultsRecognized = "no"
            voiceRecognizer.start()
            voiceRecognizer.stop()
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

    @Test
    fun destroy() {
        testRule.scenario.onActivity { activity ->
            val tv = TextView(activity)
            voiceRecognizer.init(activity, tv)
            voiceRecognizer.resultsRecognized = "no"
            voiceRecognizer.destroy()
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

    @Test
    fun onReadyForSpeech() {
        testRule.scenario.onActivity { activity ->
            val tv = TextView(activity)
            voiceRecognizer.init(activity, tv)
            voiceRecognizer.onReadyForSpeech(null)
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

    @Test
    fun onBeginningOfSpeech() {
        testRule.scenario.onActivity { activity ->
            val tv = TextView(activity)
            voiceRecognizer.init(activity, tv)
            voiceRecognizer.onBeginningOfSpeech()
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

    @Test
    fun onRmsChanged() {
        testRule.scenario.onActivity { activity ->
            val tv = TextView(activity)
            voiceRecognizer.init(activity, tv)
            voiceRecognizer.onRmsChanged(0f)
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

    @Test
    fun onBufferReceived() {
        testRule.scenario.onActivity { activity ->
            val tv = TextView(activity)
            voiceRecognizer.init(activity, tv)
            voiceRecognizer.onBufferReceived(null)
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

    @Test
    fun onEndOfSpeech() {
        testRule.scenario.onActivity { activity ->
            val tv = TextView(activity)
            voiceRecognizer.init(activity, tv)
            voiceRecognizer.onEndOfSpeech()
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

    @Test
    fun onError() {
        testRule.scenario.onActivity { activity ->
            val tv = TextView(activity)
            voiceRecognizer.init(activity, tv)
            voiceRecognizer.onError(0)
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }

    @Test
    fun onResults() {
        testRule.scenario.onActivity { activity ->
            val tv = TextView(activity)
            voiceRecognizer.init(activity, tv)
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
            val tv = TextView(activity)
            voiceRecognizer.init(activity, tv)
            val bundle = Bundle()
            val list = ArrayList<String>()
            list.add("yo")
            bundle.putStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION, list)
            voiceRecognizer.onPartialResults(bundle)
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "yo")
        }
    }

    @Test
    fun onEvent() {
        testRule.scenario.onActivity { activity ->
            val tv = TextView(activity)
            voiceRecognizer.init(activity, tv)
            voiceRecognizer.onEvent(0, null)
            Assert.assertTrue(voiceRecognizer.resultsRecognized == "")
        }
    }
}