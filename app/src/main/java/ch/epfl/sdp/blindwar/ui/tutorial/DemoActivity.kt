package ch.epfl.sdp.blindwar.ui.tutorial

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.domain.game.GameInstance
import ch.epfl.sdp.blindwar.domain.game.GameSolo
import ch.epfl.sdp.blindwar.domain.game.SongMetaData
import ch.epfl.sdp.blindwar.domain.game.Tutorial
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
    private val viewModel: SongMetadataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        // Game instance tutorial
        game = GameSolo(gameInstance, assets)
        game.init()

        // Start a music
        game.nextRound()
        game.play()
        songMetaData = game.currentMetadata()!!
        viewModel.setMeta(songMetaData)

        // Cache song image
        Picasso.get().load(songMetaData.imageUrl)

        // Get the widgets
        /** TODO : enable databindings **/
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
            startButton.setMinAndMaxFrame(30, 50)
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
            setVisibilityLayout(View.GONE)
            launchSongSummary()
        }

        // Delete the text of the guess
        guessEditText.setText("")
    }

    private fun setVisibilityLayout(code: Int) {
        audioVisualizer.visibility = code
        guessButton.visibility = code
        scoreTextView.visibility = code
        guessEditText.visibility = code
        startButton.visibility = code
    }

    private fun launchSongSummary() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(SongSummaryFragment::class.java.name)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

        val songSummary = SongSummaryFragment()
        transaction.add(R.id.fragment_container, songSummary, "Song Summary")
        transaction.commit()
    }

    private fun launchGameSummary() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(SongSummaryFragment::class.java.name)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

        val gameSummary = GameSummaryFragment()
        transaction.add(R.id.fragment_container, gameSummary, "Game Summary")
        transaction.commit()
    }

    override fun onBackPressed() {
        /**
         * TODO: Correct edge case where the number of fragment in backstack > 1
         * (can happen when restarting the activity after making changes)
         */
        // If a song summary fragment is on the screen
        // Verify that this is a SongSummaryFragment
        /**
        if (supportFragmentManager.backStackEntryCount > 0) {
            // Pass to the next music or switch to game over

            if (!game.nextRound()) {
                supportFragmentManager.popBackStackImmediate()
                setVisibilityLayout(View.VISIBLE)
                songMetaData = game.currentMetadata()!!
                viewModel.setMeta(songMetaData)
            } else {
                launchGameSummary()
            }
        }
            **/

            // If a song summary fragment is on the screen
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStackImmediate()
                setVisibilityLayout(View.VISIBLE)
                // Pass to the next music
                game.nextRound()
                viewModel.setMeta(game.currentMetadata()!!)
            }

        else super.onBackPressed();
        }
}