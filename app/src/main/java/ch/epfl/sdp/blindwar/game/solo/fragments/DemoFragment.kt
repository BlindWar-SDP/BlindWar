package ch.epfl.sdp.blindwar.game.solo.fragments

import android.app.Activity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata
import ch.epfl.sdp.blindwar.game.model.config.GameFormat
import ch.epfl.sdp.blindwar.game.model.config.GameMode
import ch.epfl.sdp.blindwar.game.solo.fragments.SongSummaryFragment.Companion.ARTIST_KEY
import ch.epfl.sdp.blindwar.game.solo.fragments.SongSummaryFragment.Companion.COVER_KEY
import ch.epfl.sdp.blindwar.game.solo.fragments.SongSummaryFragment.Companion.SUCCESS_KEY
import ch.epfl.sdp.blindwar.game.solo.fragments.SongSummaryFragment.Companion.TITLE_KEY
import ch.epfl.sdp.blindwar.game.util.VoiceRecognizer
import ch.epfl.sdp.blindwar.game.viewmodels.GameInstanceViewModel
import ch.epfl.sdp.blindwar.game.viewmodels.GameViewModel
import ch.epfl.sdp.blindwar.game.viewmodels.GameViewModelMulti
import ch.epfl.sdp.blindwar.game.viewmodels.GameViewModelSolo
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import java.util.*

/**
 * Fragment containing the UI logic of a solo game
 *
 * @constructor creates a DemoFragment
 */
class DemoFragment : Fragment() {

    // VIEW MODELS
    lateinit var gameViewModel: GameViewModel
    private val gameInstanceViewModel: GameInstanceViewModel by activityViewModels()

    // METADATA
    private lateinit var musicMetadata: MusicMetadata
    private var playing = true

    // INTERFACE
    private lateinit var gameSummary: GameSummaryFragment
    private lateinit var scoreTextView: TextView
    private lateinit var guessButton: ImageButton
    private lateinit var countDown: TextView
    private lateinit var timer: CountDownTimer

    // Animations / Buttons
    private lateinit var crossAnim: LottieAnimationView
    private lateinit var startButton: LottieAnimationView
    private lateinit var audioVisualizer: LottieAnimationView
    private lateinit var microphoneButton: ImageButton

    // Round duration
    private var duration: Int = 0

    // INPUT
    private lateinit var voiceRecognizer: VoiceRecognizer
    private var isVocal = false
    private lateinit var guessEditText: EditText

    // MODES
    // Race mode
    private lateinit var chronometer: Chronometer
    private var elapsed: Long = -1000L

    // Survival mode
    private lateinit var heartImage: ImageView
    private lateinit var heartNumber: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.activity_animated_demo, container, false)

        // Switch between the two different game view model
        when(gameInstanceViewModel.gameInstance.value?.gameFormat){
            GameFormat.SOLO -> gameViewModel = context?.let {
                GameViewModelSolo(
                    gameInstanceViewModel.gameInstance.value!!,
                    it,
                    resources
                )
            }!!
            GameFormat.MULTI -> gameViewModel = context?.let {
                GameViewModelMulti(
                    gameInstanceViewModel.gameInstance.value!!,
                    it,
                    resources
                )
            }!!
        }

        gameViewModel.init()

        // Retrieve the game duration from the GameInstance object
        duration = gameInstanceViewModel.gameInstance.value?.gameConfig
                                                           ?.parameter
                                                           ?.timeToFind!!

        // Create and start countdown
        timer = createCountDown()
        countDown = view.findViewById(R.id.countdown)

        // Mode specific interface
        val mode = gameInstanceViewModel
            .gameInstance
            .value!!
            .gameConfig
            .mode

        chronometer = view.findViewById(R.id.simpleChronometer)
        heartImage = view.findViewById(R.id.heartImage)
        heartNumber = view.findViewById(R.id.heartNumber)

        when (mode) {
            GameMode.TIMED -> initRaceMode()
            GameMode.SURVIVAL -> initSurvivalMode()
            else -> {
            }
        }

        // Get the widgets
        guessEditText = view.findViewById(R.id.guessEditText)
        scoreTextView = view.findViewById(R.id.scoreTextView)
        guessButton = view.findViewById<ImageButton>(R.id.guessButton).also {
            it.setOnClickListener {
                guessEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
                guess(isVocal, isAuto = false)
                // Delete the text of the guess
                guessEditText.setText("")
            }
        }

        startButton = view.findViewById<LottieAnimationView>(R.id.startButton).also {
            it.setOnClickListener {
                playAndPause()
            }
        }

        crossAnim = view.findViewById(R.id.cross)
        crossAnim.repeatCount = 1

        view.findViewById<ConstraintLayout>(R.id.fragment_container).setOnClickListener {
            guessEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }

        voiceRecognizer = VoiceRecognizer()
        audioVisualizer = view.findViewById(R.id.audioVisualizer)
        startButton.setMinAndMaxFrame(30, 50)

        microphoneButton = view.findViewById(R.id.microphone)
        context?.let { voiceRecognizer.init(it, Locale.ENGLISH.toLanguageTag()) }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        voiceRecognizer.resultString.observe(viewLifecycleOwner) {
            //voiceRecognizer.stop()
            guessEditText.setText(it)
            //Log.d("VOICE RECOGNITION RESULT", it)
            guess(isVocal, isAuto = false)
            isVocal = false
        }

        // Create game summary
        gameSummary = GameSummaryFragment()

        /** TODO : Settings menu
        guessEditText.doOnTextChanged { text, _, _, _ ->
        if (text != "" && (text!!.length > game.currentMetadata()?.title!!.length / 2.0)) {
        isVocal = voiceRecognizer.resultsRecognized != ""
        guess(false, isAuto = true) //guess as a keyboard at every change
        }
        } **/

        //warning seems ok, no need to override performClick
        microphoneButton.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    gameViewModel.pause()
                    voiceRecognizer.start()
                    isVocal = true
                }

                MotionEvent.ACTION_UP -> {
                    gameViewModel.play()
                    voiceRecognizer.stop()
                }
            }
            true
        }

        // Start the game
        gameViewModel.nextRound()
        gameViewModel.play()
        musicMetadata = gameViewModel.currentMetadata()!!
        guessEditText.hint = musicMetadata.artist
        timer.start()

        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * Creates the countdown timer
     *
     * @return creates countdown timer with the duration of the round
     */
    private fun createCountDown(): CountDownTimer {
        return object : CountDownTimer(duration.toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                duration = millisUntilFinished.toInt()
                elapsed += 1000
                //Log.d("DURATION", duration.toString())
                countDown.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                gameViewModel.timeout()
                this.cancel()
                launchSongSummary(success = false)
            }
        }
    }

    /**
     * Set the demo fragment visibility
     *
     * @param code visibility code : either VISIBLE or GONE
     */
    private fun setVisibilityLayout(code: Int) {
        guessButton.visibility = code
        scoreTextView.visibility = code
        guessEditText.visibility = code
        setAnimVisibility(code)
    }

    /**
     * Handle pause and resume game logic
     */
    private fun playAndPause() {
        playing = if (playing) {
            gameViewModel.pause()
            pauseAnim()
            timer.cancel()
            timer = createCountDown()
            chronometer.stop()
            false

        } else {
            gameViewModel.play()
            resumeAnim()
            restartChronometer()
            timer.start()
            true
        }
    }

    // RACE MODE
    private fun restartChronometer() {
        chronometer.base = SystemClock.elapsedRealtime() - elapsed
        chronometer.start()
    }

    private fun initRaceMode() {
        chronometer.visibility = View.VISIBLE
        chronometer.start()
    }

    // SURVIVAL MODE
    private fun initSurvivalMode() {
        heartImage.visibility = View.VISIBLE
        heartNumber.visibility = View.VISIBLE
        gameViewModel.lives.observe(requireActivity()) {
            heartNumber.text = "x $it"
        }
    }

    /**
     * Verify the guess of the player
     *
     * @param isVocal true is player used its microphone to guess
     * @param isAuto true if autoGuessing is activated
     */
    private fun guess(isVocal: Boolean, isAuto: Boolean) {
        if (gameViewModel.guess(guessEditText.text.toString(), isVocal)) {
            // Update the number of point view
            scoreTextView.text = gameViewModel.score.toString()
            (activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(view?.windowToken, 0)
            launchSongSummary(success = true)
        } else if (!isAuto) {
            animNotFound()
        }
    }

    /** Launches the Game Over fragment after a song
     *
     * @param success indicates if the user found the sound or not
     */
    private fun launchSongSummary(success: Boolean) {
        setVisibilityLayout(View.GONE)
        timer.cancel()
        chronometer.stop()

        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.addToBackStack("DEMO")
        transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

        val songSummary = SongSummaryFragment()
        songSummary.arguments = createBundleSongSummary(success)

        transaction?.add((view?.parent as ViewGroup).id, songSummary, "Song Summary")
        transaction?.commit()
    }

    /**
     * Creates the bundle providing metadata arguments to a Song Summary fragment
     */
    private fun createBundleSongSummary(success: Boolean): Bundle {
        val bundle = Bundle()
        bundle.putString(ARTIST_KEY, musicMetadata.artist)
        bundle.putString(TITLE_KEY, musicMetadata.title)
        bundle.putString(COVER_KEY, musicMetadata.imageUrl)
        bundle.putBoolean(SUCCESS_KEY, success)
        return bundle
    }

    /**
     * Launches the Game Over fragment after a game
     */
    private fun launchGameSummary() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction?.replace((view?.parent as ViewGroup).id, gameSummary, "Game Summary")
        transaction?.commit()
    }

    // LIFECYCLE

    /**
     * Handle next round logic
     */
    override fun onResume() {
        super.onResume()
        val songRecord = SongSummaryFragment()
        if (activity?.supportFragmentManager?.fragments!!.size > 1) {
            if (activity?.supportFragmentManager?.fragments?.get(1) is SongSummaryFragment) {
                val songFragment = (activity?.supportFragmentManager?.fragments?.get(1) as SongSummaryFragment)
                val bundle = createBundleSongSummary(songFragment.success())

                duration = gameInstanceViewModel.gameInstance.value?.gameConfig
                                                                   ?.parameter
                                                                   ?.timeToFind!!
                restartChronometer()
                bundle.putBoolean("liked", songFragment.liked())
                songRecord.arguments = bundle
                gameSummary.setSongFragment(songRecord)

                if (!gameViewModel.nextRound()) {
                    setVisibilityLayout(View.VISIBLE)
                    // Pass to the next music
                    musicMetadata = gameViewModel.currentMetadata()!!
                    guessEditText.hint = musicMetadata.artist
                    guessEditText.setText("")
                    timer = createCountDown()
                    timer.start()
                } else {
                    launchGameSummary()
                }
            }
        }
    }

    override fun onPause() {
        if (playing)
            playAndPause()
        super.onPause()
    }

    override fun onDestroy() {
        timer.cancel()
        voiceRecognizer.destroy()
        super.onDestroy()
    }

    // ANIMATIONS
    /**
     * Setter for the animation visibility
     *
     * @param code visibility code : VISIBLE or GONE
     */
    private fun setAnimVisibility(code: Int) {
        crossAnim.visibility = code
        countDown.visibility = code
        audioVisualizer.visibility = code
        startButton.visibility = code
        microphoneButton.visibility = code
        view?.findViewById<ImageButton>(R.id.guessButton)?.visibility = code
    }

    /**
     * Animation played when the player has a wrong guess
     */
    private fun animNotFound() {
        /** Resets the base frame value of the animation and keep the reversing mode **/
        crossAnim.repeatMode = LottieDrawable.RESTART
        crossAnim.repeatMode = LottieDrawable.REVERSE
        crossAnim.playAnimation()
    }

    /**
     * Animation played when the player pauses the game
     */
    private fun pauseAnim() {
        audioVisualizer.pauseAnimation()
        startButton.setMinAndMaxFrame(30, 55)
        startButton.repeatCount = 0
        startButton.playAnimation()
    }

    /**
     * Animation played when the player pauses the game
     */
    private fun resumeAnim() {
        audioVisualizer.resumeAnimation()
        startButton.setMinAndMaxFrame(10, 25)
        startButton.playAnimation()
    }
}