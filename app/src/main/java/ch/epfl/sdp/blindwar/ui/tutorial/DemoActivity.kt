package ch.epfl.sdp.blindwar.ui.tutorial

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.domain.game.GameSolo
import ch.epfl.sdp.blindwar.domain.game.SongMetaData
import ch.epfl.sdp.blindwar.domain.game.Tutorial.gameInstance
import ch.epfl.sdp.blindwar.ui.viewmodel.SongMetadataViewModel
import com.airbnb.lottie.LottieAnimationView
import com.squareup.picasso.Picasso

class DemoActivity: AppCompatActivity() {
    /** TODO: Refactor Game class to avoid this encapsulation leak **/
    lateinit var game: GameSolo
    private var playing = true
    private lateinit var guessEditText: EditText
    private lateinit var scoreTextView: TextView
    private lateinit var songMetaData: SongMetaData
    private lateinit var guessButton: Button
    private lateinit var startButton: LottieAnimationView
    private lateinit var audioVisualizer: LottieAnimationView
    private lateinit var countDown: TextView
    private lateinit var timer: CountDownTimer
    private var duration: Int = 0
    private val viewModel: SongMetadataViewModel by viewModels()

    private lateinit var gameSummary: GameSummaryFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        countDown = findViewById(R.id.countdown)
        // Game instance tutorial
        game = GameSolo(gameInstance, assets)
        game.init()


        duration = gameInstance
            .gameConfig
            .parameter
            .timeToFind

        // Start a music
        game.nextRound()
        game.play()
        songMetaData = game.currentMetadata()!!
        viewModel.setMeta(songMetaData)

        // Create and start countdown
        timer = createCountDown().start()

        /**
        // Cache song image
        Picasso.get().load(songMetaData.imageUrl)
        **/

        // Create game summary
        gameSummary = GameSummaryFragment()

        // Get the widgets
        guessEditText = findViewById(R.id.guessEditText)
        scoreTextView = findViewById(R.id.scoreTextView)

        startButton = findViewById(R.id.startButton)
        audioVisualizer = findViewById(R.id.audioVisualizer)
        startButton.setMinAndMaxFrame(30, 50)
        guessButton = findViewById(R.id.guessButton)
    }

    fun playAndPause(view: View) {
        playing = if(playing) {
            game.pause()
            audioVisualizer.pauseAnimation()
            startButton.setMinAndMaxFrame(30, 55)
            //startButton.repeatCount = 0
            startButton.playAnimation()
            false

        } else {
            game.play()
            audioVisualizer.resumeAnimation()
            startButton.setMinAndMaxFrame(10, 25)
            startButton.playAnimation()
            true
        }
    }

    override fun onPause() {
        super.onPause()
        game.pause()
    }

    fun guess(view: View) {
        // Try to guess
        //Log.d("guesses", guessEditText.text.toString())
        if(game.guess(guessEditText.text.toString())) {
            // Update the number of point view
            scoreTextView.text = game.score.toString()
            launchSongSummary(success = true)
        }

        // Delete the text of the guess
        guessEditText.setText("")
    }

    private fun createCountDown(): CountDownTimer {
        return object : CountDownTimer(duration.toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                countDown.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                game.timeout()
                this.cancel()
                launchSongSummary(success = false)
            }
        }
    }

    private fun setVisibilityLayout(code: Int) {
        countDown.visibility = code
        audioVisualizer.visibility = code
        guessButton.visibility = code
        scoreTextView.visibility = code
        guessEditText.visibility = code
        startButton.visibility = code
    }

    private fun launchSongSummary(success: Boolean) {
        setVisibilityLayout(View.GONE)
        timer.cancel()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(SongSummaryFragment::class.java.name)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

        val songSummary = SongSummaryFragment()
        songSummary.arguments = createBundleSongSummary(success)

        transaction.add(R.id.fragment_container, songSummary, "Song Summary")
        transaction.commit()
    }

    private fun launchGameSummary() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

        transaction.add(R.id.fragment_container, gameSummary, "Game Summary")
        transaction.commit()
    }

    private fun createBundleSongSummary(success: Boolean): Bundle {
        val bundle = Bundle()
        bundle.putString("artist", viewModel.selectedMetadata.value?.artist)
        bundle.putString("title", viewModel.selectedMetadata.value?.title)
        bundle.putString("image", viewModel.selectedMetadata.value?.imageUrl)
        bundle.putBoolean("success", success)
        return bundle
    }

    /**
     * TODO: Fix some behaviors depending on the type and number of fragments
     */
    override fun onBackPressed() {
            // If a song summary fragment is on the screen
            if (supportFragmentManager.backStackEntryCount > 0
                && supportFragmentManager.fragments[0] is SongSummaryFragment) {

                /** Make it as a function **/
                val songRecord = SongSummaryFragment()
                val songFragment = (supportFragmentManager.fragments[0] as SongSummaryFragment)
                val bundle = createBundleSongSummary(songFragment.success())

                bundle.putBoolean("liked", songFragment.liked())
                songRecord.arguments = bundle
                gameSummary.setSongFragment(songRecord)
                supportFragmentManager.popBackStackImmediate()

                    if (!game.nextRound()) {
                        setVisibilityLayout(View.VISIBLE)
                        // Pass to the next music
                        viewModel.setMeta(game.currentMetadata()!!)
                        // Cache song image
                        Picasso.get().load(viewModel.selectedMetadata.value?.imageUrl)
                        timer.start()
                    } else {
                        launchGameSummary()
                    }
            }

        else super.onBackPressed();
        }
}