package ch.epfl.sdp.blindwar.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.method.ScrollingMovementMethod
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.domain.game.VoiceRecognizer
import org.vosk.LibVosk
import org.vosk.LogLevel
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.RecognitionListener
import org.vosk.android.SpeechService
import org.vosk.android.SpeechStreamService
import org.vosk.android.StorageService
import java.io.IOException


class VoskActivity : Activity(), RecognitionListener {
    private var model: Model? = null
    private var speechService: SpeechService? = null
    private var speechStreamService: SpeechStreamService? = null
    private var resultView: TextView? = null
    private val voiceRecognizer: VoiceRecognizer = VoiceRecognizer()
    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        setContentView(R.layout.main)

        // Setup layout
        resultView = findViewById(R.id.result_text)
        val resultEditText = findViewById<EditText>(R.id.result_text_edit)
        setUiState(STATE_START)
        findViewById<View>(R.id.recognize_mic).setOnClickListener { recognizeMicrophone() }
        (findViewById<View>(R.id.pause) as ToggleButton).setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            pause(isChecked)
        }
        LibVosk.setLogLevel(LogLevel.INFO)

        // Check if user has given permission to record audio, init the model after permission is granted
        val permissionCheck =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.RECORD_AUDIO)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSIONS_REQUEST_RECORD_AUDIO
            )
        } else initModel()
        //recognizer with google
        voiceRecognizer.init(this, resultEditText)

        findViewById<View>(R.id.recognize_mic_google).setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    voiceRecognizer.start()
                }
                MotionEvent.ACTION_UP -> {
                    v?.performClick()
                    voiceRecognizer.stop()
                }
            }
            v?.onTouchEvent(event) ?: true
        }
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                val result = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS
                )
                resultView?.text = result!![0]
            }
        }
    }

    private fun initModel() {
        StorageService.unpack(this, "model-en-us", "model",
            { model: Model? ->
                this.model = model
                setUiState(STATE_READY)
            }
        ) { exception: IOException ->
            setErrorState(
                "Failed to unpack the model" + exception.message
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Recognizer initialization is a time-consuming and it involves IO,
                // so we execute it in async task
                initModel()
            } else {
                finish()
            }
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        voiceRecognizer.destroy()
        if (speechService != null) {
            speechService!!.stop()
            speechService!!.shutdown()
        }
        if (speechStreamService != null) {
            speechStreamService!!.stop()
        }
    }

    override fun onResult(hypothesis: String) {
        resultView!!.text = hypothesis.trimIndent()
    }

    override fun onFinalResult(hypothesis: String) {
        resultView!!.text = hypothesis.trimIndent()
        setUiState(STATE_DONE)
        if (speechStreamService != null) {
            speechStreamService = null
        }
    }

    override fun onPartialResult(hypothesis: String) {
    }

    override fun onError(e: Exception) {
        setErrorState(e.message)
    }

    override fun onTimeout() {
        setUiState(STATE_DONE)
    }

    private fun setUiState(state: Int) {
        when (state) {
            STATE_START -> {
                resultView?.setText(R.string.preparing)
                resultView!!.movementMethod = ScrollingMovementMethod()
                findViewById<View>(R.id.recognize_mic).isEnabled = false
                findViewById<View>(R.id.recognize_mic_google).isEnabled = false
                findViewById<View>(R.id.pause).isEnabled = false
            }
            STATE_READY -> {
                resultView?.setText(R.string.ready)
                (findViewById<View>(R.id.recognize_mic) as Button).setText(R.string.recognize_microphone_vosk)
                findViewById<View>(R.id.recognize_mic).isEnabled = true
                (findViewById<View>(R.id.recognize_mic_google) as Button).setText(R.string.recognize_microphone_google)
                findViewById<View>(R.id.recognize_mic_google).isEnabled = true
                findViewById<View>(R.id.pause).isEnabled = false
            }
            STATE_DONE -> {
                (findViewById<View>(R.id.recognize_mic) as Button).setText(R.string.recognize_microphone_vosk)
                findViewById<View>(R.id.recognize_mic).isEnabled = true
                (findViewById<View>(R.id.recognize_mic_google) as Button).setText(R.string.recognize_microphone_google)
                findViewById<View>(R.id.recognize_mic_google).isEnabled = true
                findViewById<View>(R.id.pause).isEnabled = false
            }
            STATE_MIC_VOSK -> {
                (findViewById<View>(R.id.recognize_mic) as Button).setText(R.string.stop_microphone)
                resultView!!.text = getString(R.string.say_something)
                findViewById<View>(R.id.recognize_mic).isEnabled = true
                findViewById<View>(R.id.pause).isEnabled = true
            }
            STATE_MIC_GOOGLE -> {
                (findViewById<View>(R.id.recognize_mic_google) as Button).setText(R.string.stop_microphone)
                resultView!!.text = getString(R.string.say_something)
                findViewById<View>(R.id.recognize_mic_google).isEnabled = true
                findViewById<View>(R.id.pause).isEnabled = true
            }
            else -> throw IllegalStateException("Unexpected value: $state")
        }
    }

    private fun setErrorState(message: String?) {
        resultView!!.text = message
        (findViewById<View>(R.id.recognize_mic) as Button).setText(R.string.recognize_microphone_vosk)
        (findViewById<View>(R.id.recognize_mic_google) as Button).setText(R.string.recognize_microphone_google)
        findViewById<View>(R.id.recognize_mic).isEnabled = false
        findViewById<View>(R.id.recognize_mic_google).isEnabled = false
    }

    private fun recognizeMicrophone() {
        if (speechService != null) {
            setUiState(STATE_DONE)
            speechService!!.stop()
            speechService = null
        } else {
            setUiState(STATE_MIC_VOSK)
            try {
                val rec = Recognizer(model, 16000.0f)
                speechService = SpeechService(rec, 16000.0f)
                speechService!!.startListening(this)
            } catch (e: IOException) {
                setErrorState(e.message)
            }
        }
    }

    private fun pause(checked: Boolean) {
        if (speechService != null) {
            speechService!!.setPause(checked)
        }
    }

    companion object {
        private const val STATE_START = 0
        private const val STATE_READY = 1
        private const val STATE_DONE = 2
        private const val STATE_MIC_VOSK = 4
        private const val STATE_MIC_GOOGLE = 5

        /* Used to handle permission request */
        private const val PERMISSIONS_REQUEST_RECORD_AUDIO = 1
    }
}