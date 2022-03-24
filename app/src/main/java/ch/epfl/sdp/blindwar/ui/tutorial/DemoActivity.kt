package ch.epfl.sdp.blindwar.ui.tutorial

import android.opengl.Visibility
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.domain.game.GameSolo
import ch.epfl.sdp.blindwar.domain.game.SongMetaData
import ch.epfl.sdp.blindwar.domain.game.Tutorial.gameInstance
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.airbnb.lottie.LottieDrawable.RESTART
import com.airbnb.lottie.LottieDrawable.REVERSE
import com.squareup.picasso.Picasso
import kotlin.math.truncate

class DemoActivity: AppCompatActivity() {
    /** TODO: Refactor Game class to avoid this encapsulation leak **/
    internal lateinit var game: GameSolo
    private var playing = true
    private lateinit var guessEditText: EditText
    private lateinit var scoreTextView: TextView
    private lateinit var songMetaData: SongMetaData
    private lateinit var guessButton: Button
    private lateinit var crossAnim: LottieAnimationView
    private lateinit var startButton: LottieAnimationView
    private lateinit var audioVisualizer: LottieAnimationView
    private lateinit var countDown: TextView
    //private lateinit var timer: CountDownTimer
    private var duration: Int = 0
    private var toggle: Boolean = false

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

        // Create and start countdown
       // timer = createCountDown().start()

        // Cache song image
        //Picasso.get().load(songMetaData.imageUrl)

        // Create game summary
        gameSummary = GameSummaryFragment()

        // Get the widgets
        guessEditText = findViewById(R.id.guessEditText)
        guessEditText.hint = songMetaData.artist
        scoreTextView = findViewById(R.id.scoreTextView)

        //crossAnim = findViewById(R.id.cross)
        //crossAnim.repeatCount = 1

        startButton = findViewById(R.id.startButton)
        //startButton.setMinAndMaxFrame(30, 50)
        audioVisualizer = findViewById(R.id.audioVisualizer)
        guessButton = findViewById(R.id.guessButton)
    }

    fun playAndPause(view: View) {
        playing = if(playing) {
            game.pause()
            //audioVisualizer.pauseAnimation()
            //startButton.setMinAndMaxFrame(30, 55)
            //startButton.repeatCount = 0
            //startButton.playAnimation()
            false

        } else {
            game.play()
            //audioVisualizer.resumeAnimation()
            //startButton.setMinAndMaxFrame(10, 25)
            //startButton.playAnimation()
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
        } else {
            /** Resets the base frame value of the animation and keep the reversing mode **/
            //crossAnim.repeatMode = RESTART
            //crossAnim.repeatMode = REVERSE
            //crossAnim.playAnimation()
        }

        // Delete the text of the guess
        guessEditText.setText("")
    }

    /**
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
    **/

    private fun setVisibilityLayout(code: Int) {
        //crossAnim.visibility = code
        countDown.visibility = code
        //audioVisualizer.visibility = code
        guessButton.visibility = code
        scoreTextView.visibility = code
        guessEditText.visibility = code
        startButton.visibility = code
    }

    private fun launchSongSummary(success: Boolean) {
        setVisibilityLayout(View.GONE)
        //timer.cancel()

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
        bundle.putString("artist", songMetaData.artist)
        bundle.putString("title", songMetaData.title)
        bundle.putString("image", songMetaData.imageUrl)
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

                //bundle.putBoolean("liked", songFragment.liked())
                songRecord.arguments = bundle
                gameSummary.setSongFragment(songRecord)
                supportFragmentManager.popBackStackImmediate()

                    if (!game.nextRound()) {
                        setVisibilityLayout(View.VISIBLE)
                        // Pass to the next music
                        songMetaData = game.currentMetadata()!!
                        guessEditText.hint = songMetaData.artist
                        // Cache song image
                        // Picasso.get().load(viewModel.selectedMetadata.value?.imageUrl)
                        //timer.start()
                    } else {
                        launchGameSummary()
                    }
            }

            else {
                //timer.cancel()
                super.onBackPressed();
            }
        }

    override fun onDestroy() {
        //timer.cancel()
        super.onDestroy()
    }
}