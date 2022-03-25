package ch.epfl.sdp.blindwar.ui.tutorial

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import ch.epfl.sdp.blindwar.R
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable.RESTART
import com.airbnb.lottie.LottieDrawable.REVERSE

class AnimatedDemoActivity : DemoActivity() {
    private lateinit var crossAnim: LottieAnimationView
    private lateinit var startButton: LottieAnimationView
    private lateinit var audioVisualizer: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animated_demo)

        crossAnim = findViewById(R.id.cross)
        crossAnim.repeatCount = 1

        startButton = findViewById(R.id.startButton)
        audioVisualizer = findViewById(R.id.audioVisualizer)
        startButton.setMinAndMaxFrame(30, 50)

        super.guessEditText = findViewById<EditText>(R.id.guessEditText).let{
            it.hint = super.game.currentMetadata()?.artist
            it
        }
        super.scoreTextView = findViewById(R.id.scoreTextView)
        super.countDown = findViewById(R.id.countdown)
    }

    fun playAndPause(view: View) {
        super.playing = if(playing) {
            super.game.pause()
            pauseAnim()
            false

        } else {
            super.game.play()
            resumeAnim()
            true
        }
    }

    /** Override animation hooks **/
    override fun disableAnim(code: Int) {
        crossAnim.visibility = code
        countDown.visibility = code
        audioVisualizer.visibility = code
        startButton.visibility = code
        findViewById<Button>(R.id.guessButton).visibility = code
    }

    override fun animNotFound() {
        /** Resets the base frame value of the animation and keep the reversing mode **/
        crossAnim.repeatMode = RESTART
        crossAnim.repeatMode = REVERSE
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