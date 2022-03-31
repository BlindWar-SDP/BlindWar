package ch.epfl.sdp.blindwar.ui.solo

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import ch.epfl.sdp.blindwar.R
import com.airbnb.lottie.Lottie
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable

class AnimatedDemoFragment: DemoFragment() {
    private lateinit var crossAnim: LottieAnimationView
    private lateinit var startButton: LottieAnimationView
    private lateinit var audioVisualizer: LottieAnimationView

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
            it.setOnClickListener{
                playAndPause()
            }
        }

        audioVisualizer = view.findViewById(R.id.audioVisualizer)
        startButton.setMinAndMaxFrame(30, 50)

        super.guessEditText = view.findViewById<EditText>(R.id.guessEditText).let{
            it.hint = super.game.currentMetadata()?.artist
            it
        }
        super.scoreTextView = view.findViewById(R.id.scoreTextView)
        super.countDown = view.findViewById(R.id.countdown)
        super.guessButton = view.findViewById<ImageButton>(R.id.guessButton).also {
            it.setOnClickListener{
                super.guess()
            }
        }

        return view
    }

    private fun playAndPause() {
        super.playing = if(playing) {
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