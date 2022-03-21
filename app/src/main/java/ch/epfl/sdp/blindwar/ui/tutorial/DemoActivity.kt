package ch.epfl.sdp.blindwar.ui.tutorial

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.domain.game.GameTutorial
import ch.epfl.sdp.blindwar.domain.game.SongMetaData

class DemoActivity: AppCompatActivity() {
    /** TODO: Refactor Game class to avoid this encapsulation leak **/
    lateinit var game: GameTutorial
    private var playing = true
    private lateinit var guessEditText: EditText
    private lateinit var scoreTextView: TextView
    private lateinit var songMetaData: SongMetaData
    private lateinit var guessButton: Button
    private lateinit var startButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        // Create the game instance with difficulty easy
        game = GameTutorial(assets, 5000)

        // Start a music
        game.nextRound()
        game.play()
        songMetaData = game.currentMetaData!!

        // Get the widgets
        /** TODO : enable databindings **/
        guessEditText = findViewById(R.id.guessEditText)
        scoreTextView = findViewById(R.id.scoreTextView)

        startButton = findViewById(R.id.startButton)
        guessButton = findViewById(R.id.guessButton)
    }

    fun playAndPause(view: View) {
        playing = if(playing) {
            game.pause()
            false
        } else {
            game.play()
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
        guessButton.visibility = code
        scoreTextView.visibility = code
        guessEditText.visibility = code
        startButton.visibility = code
    }

    private fun launchSongSummary() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(SongSummaryFragment::class.java.name)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

        val bundle: Bundle = Bundle()
        bundle.putString("Artist", songMetaData.artist)
        bundle.putString("Title", songMetaData.title)
        bundle.putString("Image", songMetaData.imageUrl)

        val songSummary = SongSummaryFragment()
        songSummary.arguments = bundle
        transaction.add(R.id.fragment_container, songSummary, "Song Summary")
        transaction.commit()
    }

    override fun onBackPressed() {
        // If a song summary fragment is on the screen
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
            setVisibilityLayout(View.VISIBLE)
            // Pass to the next music
            songMetaData = game.nextRound()!!
        }

        else super.onBackPressed();
    }
}