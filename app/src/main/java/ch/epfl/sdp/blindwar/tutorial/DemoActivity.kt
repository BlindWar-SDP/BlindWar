package ch.epfl.sdp.blindwar.tutorial

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.GameTutorial
import ch.epfl.sdp.blindwar.game.MusicMetaData

class DemoActivity : AppCompatActivity() {
    private lateinit var game: GameTutorial
    private var playing = true
    private lateinit var guessEditText: EditText
    private lateinit var scoreTextView: TextView
    private lateinit var musicMetaData: MusicMetaData
    //private lateinit var visualizer: CircleVisualizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        // Create the game instance with difficulty easy
        this.game = GameTutorial(assets, 5000)

        // Start a music
        this.game.nextRound()
        this.game.play()

        // Get the widgets
        this.guessEditText = findViewById(R.id.guessEditText)
        this.scoreTextView = findViewById(R.id.scoreTextView)
        this.scoreTextView.text = "test"
        this.guessEditText = findViewById(R.id.guessEditText)
        this.scoreTextView = findViewById(R.id.scoreTextView)
        this.scoreTextView.setText("test")

        /**

        this.visualizer = findViewById(R.id.visualizer)
        visualizer.setStrokeWidth(2)
        visualizer.setColor(ContextCompat.getColor(this, R.color.white))

        // Set your media player to the visualizer.
        visualizer.setPlayer(game.sessionId)

         **/
    }

    fun playAndPause(view: View) {
        if (playing) {
            this.game.pause()
            this.playing = false
        } else {
            this.game.play()
            this.playing = true
        }
    }

    override fun onPause() {
        super.onPause()
        this.game.pause()
    }

    fun guess(view: View) {
        // Try to guess
        if (this.game.guess(guessEditText.text.toString())) {
            // Update the number of point view
            //this.scoreTextView.setText(this.game.score)

            // Pass to the next music
            this.musicMetaData = this.game.nextRound()!!
        }

        // Delete the text of the guess
        this.guessEditText.setText("")
    }
}