package ch.epfl.sdp.blindwar.ui.tutorial

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.domain.game.GameTutorial
import ch.epfl.sdp.blindwar.domain.game.SongMetaData
import ch.epfl.sdp.blindwar.domain.game.Tutorial.gameInstance
import ch.epfl.sdp.blindwar.ui.solo.GameInstanceViewModel

open class DemoActivity: AppCompatActivity() {
    lateinit var game: GameTutorial
    protected var playing = true
    protected lateinit var guessEditText: EditText
    protected lateinit var scoreTextView: TextView
    protected lateinit var songMetaData: SongMetaData
    private lateinit var guessButton: ImageButton
    protected lateinit var countDown: TextView
    private var duration: Int = 0
    private lateinit var timer: CountDownTimer

    private lateinit var gameSummary: GameSummaryFragment
    private val gameInstanceViewModel: GameInstanceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        /** Set up the interface **/
        // Game instance tutorial
        game = GameTutorial(gameInstance, assets)
        game.init()

        duration = gameInstance
            .gameConfig
            .difficulty
            .timeToFind

        // Create and start countdown
        timer = createCountDown()
        countDown = findViewById(R.id.countdown)

        // Cache song image
        //Picasso.get().load(songMetaData.imageUrl)

        // Create game summary
        gameSummary = GameSummaryFragment()

        /** Fetch round metadata **/
        game.nextRound()
        /** Start the game **/
        game.play()
        timer.start()

        songMetaData = game.currentMetadata()!!

        // Get the widgets
        guessEditText = findViewById(R.id.guessEditText)
        guessEditText.hint = songMetaData.artist
        scoreTextView = findViewById(R.id.scoreTextView)
        guessButton = findViewById<ImageButton>(R.id.guessButtonDemo).also{
            it.setOnClickListener{
                guess()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        game.pause()
    }

    open fun guess() {
        if(game.guess(guessEditText.text.toString())) {
            // Update the number of point view
            scoreTextView.text = game.score.toString()
            launchSongSummary(success = true)
        } else {
            animNotFound()
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
        guessButton.visibility = code
        scoreTextView.visibility = code
        guessEditText.visibility = code
        disableAnim(code)
    }

    protected fun launchSongSummary(success: Boolean) {
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

            bundle.putBoolean("liked", songFragment.liked())
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
                timer.start()
            } else {
                launchGameSummary()
            }
        }

        else {
            timer.cancel()
            super.onBackPressed();
        }
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }

    /** Temporary solution : Animation hooks **/
    open fun animNotFound() {}
    open fun disableAnim(code: Int) {}
    open fun pauseAnim() {}
    open fun resumeAnim() {}
}