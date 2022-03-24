package ch.epfl.sdp.blindwar.domain.game

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
}