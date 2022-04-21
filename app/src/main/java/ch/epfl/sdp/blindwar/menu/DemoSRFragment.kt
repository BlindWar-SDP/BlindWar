package ch.epfl.sdp.blindwar.menu

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.util.VoiceRecognizer
import java.util.*


/**
 * Fragment that is used to showcase the SR capabilities of the app
 * TODO: CodeClimate issues / Cirrus warnings
 *
 * @constructor creates a DemoSRFragment
 */
class DemoSRFragment : Fragment() {
    private val voiceRecognizerEnglish: VoiceRecognizer = VoiceRecognizer()
    private val voiceRecognizerFrench: VoiceRecognizer = VoiceRecognizer()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_sr, container, false)
        // Setup layout
        val resultEditText = view?.findViewById<EditText>(R.id.result_text_edit_sr)!!

        // Check if user has given permission to record audio, init the model after permission is granted
        val permissionCheck =
            ContextCompat.checkSelfPermission(requireActivity().applicationContext, Manifest.permission.RECORD_AUDIO)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSIONS_REQUEST_RECORD_AUDIO
            )
        }
        //recognizer with google
        voiceRecognizerEnglish.init(requireActivity(), resultEditText, Locale.ENGLISH.toLanguageTag())
        voiceRecognizerFrench.init(requireActivity(), resultEditText, Locale.FRENCH.toLanguageTag())

        view.findViewById<View>(R.id.recognize_mic_fr)?.setOnTouchListener { v, event ->
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

        view.findViewById<View>(R.id.recognize_mic_en).setOnTouchListener { v, event ->
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

        return view
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