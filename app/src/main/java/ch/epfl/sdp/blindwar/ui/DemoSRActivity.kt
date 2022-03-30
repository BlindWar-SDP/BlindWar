package ch.epfl.sdp.blindwar.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.domain.game.VoiceRecognizer
import java.util.*


class DemoSRActivity : Activity() {
    val voiceRecognizerEnglish: VoiceRecognizer = VoiceRecognizer()
    val voiceRecognizerFrench: VoiceRecognizer = VoiceRecognizer()

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        setContentView(R.layout.activity_sr)
        // Setup layout
        val resultEditText = findViewById<EditText>(R.id.result_text_edit)

        // Check if user has given permission to record audio, init the model after permission is granted
        val permissionCheck =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.RECORD_AUDIO)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSIONS_REQUEST_RECORD_AUDIO
            )
        }
        //recognizer with google
        voiceRecognizerEnglish.init(this, resultEditText, Locale.ENGLISH.toLanguageTag())
        voiceRecognizerFrench.init(this, resultEditText, Locale.FRENCH.toLanguageTag())

        findViewById<View>(R.id.recognize_mic_fr).setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    voiceRecognizerFrench.start()
                }
                MotionEvent.ACTION_UP -> {
                    v?.performClick()
                    voiceRecognizerFrench.stop()
                }
            }
            v?.onTouchEvent(event) ?: true
        }
        findViewById<View>(R.id.recognize_mic_en).setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    voiceRecognizerEnglish.start()
                }
                MotionEvent.ACTION_UP -> {
                    v?.performClick()
                    voiceRecognizerEnglish.stop()
                }
            }
            v?.onTouchEvent(event) ?: true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceRecognizerEnglish.destroy()
        voiceRecognizerFrench.destroy()
    }

    companion object {
        /* Used to handle permission request */
        private const val PERMISSIONS_REQUEST_RECORD_AUDIO = 1
    }
}