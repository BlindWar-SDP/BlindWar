package ch.epfl.sdp.blindwar.ui.solo.animated

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.domain.game.VoiceRecognizer
import ch.epfl.sdp.blindwar.ui.solo.DemoFragment
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import java.util.*

class AnimatedDemoFragment : DemoFragment() {
    private lateinit var crossAnim: LottieAnimationView
    private lateinit var startButton: LottieAnimationView
    private lateinit var audioVisualizer: LottieAnimationView
    private lateinit var microphoneButton: ImageButton
    private lateinit var voiceRecognizer: VoiceRecognizer
    private var isVocal = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.activity_animated_demo, container, false)

        crossAnim = view.findViewById(R.id.cross)
        crossAnim.repeatCount = 1

        startButton = view.findViewById<LottieAnimationView>(R.id.startButton).also {
            it.setOnClickListener {
                playAndPause()
            }
        }

        view.findViewById<ConstraintLayout>(R.id.fragment_container).setOnClickListener{
            super.guessEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }

        voiceRecognizer = VoiceRecognizer()
        audioVisualizer = view.findViewById(R.id.audioVisualizer)
        startButton.setMinAndMaxFrame(30, 50)

        super.guessEditText = view.findViewById<EditText>(R.id.guessEditText).let {
            it.hint = super.game.currentMetadata()?.artist
            it
        }

        /** TODO: Implement Settings menu to activate auto guessing **/
        /** super.guessEditText.doOnTextChanged { text, _, _, _ ->
            if (text != "" && (text!!.length > super.game.currentMetadata()?.title!!.length / 2.0)) {
                isVocal = voiceRecognizer.resultsRecognized != ""
                super.guess(false, isAuto = true) //guess as a keyboard at every change
            }
        } **/

        super.scoreTextView = view.findViewById(R.id.scoreTextView)
        super.countDown = view.findViewById(R.id.countdown)
        super.guessButton = view.findViewById<ImageButton>(R.id.guessButton).also {
            it.setOnClickListener {
                super.guessEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
                super.guess(isVocal, isAuto = false)
            }
        }

        microphoneButton = view.findViewById(R.id.microphone)
        context?.let { voiceRecognizer.init(it, guessEditText, Locale.ENGLISH.toLanguageTag()) }
        //warning seems ok, no need to override performClick
        microphoneButton.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    super.game.pause()
                    voiceRecognizer.start()
                    isVocal = true
                }
                MotionEvent.ACTION_UP -> {
                    voiceRecognizer.stop()
                    super.game.play()
                    super.guess(isVocal, isAuto = false)
                    isVocal = false
                }
            }
            true
        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceRecognizer.destroy()
    }

    private fun playAndPause() {
        super.playing = if (playing) {
            super.game.pause()
            pauseAnim()
            super.timer.cancel()
            super.timer = super.createCountDown()
            false

        } else {
            super.game.play()
            resumeAnim()
            super.timer.start()
            true
        }
    }

    /** Override animation hooks **/
    override fun setAnimVisibility(code: Int) {
        crossAnim.visibility = code
        countDown.visibility = code
        audioVisualizer.visibility = code
        startButton.visibility = code
        microphoneButton.visibility = code
        view?.findViewById<ImageButton>(R.id.guessButton)?.visibility = code
    }

    override fun animNotFound() {
        /** Resets the base frame value of the animation and keep the reversing mode **/
        crossAnim.repeatMode = LottieDrawable.RESTART
        crossAnim.repeatMode = LottieDrawable.REVERSE
        crossAnim.playAnimation()
    }

    override fun pauseAnim() {
        audioVisualizer.pauseAnimation()
        startButton.setMinAndMaxFrame(30, 55)
        startButton.repeatCount = 0
        startButton.playAnimation()
    }

    override fun resumeAnim() {
        audioVisualizer.resumeAnimation()
        startButton.setMinAndMaxFrame(10, 25)
        startButton.playAnimation()
    }
}