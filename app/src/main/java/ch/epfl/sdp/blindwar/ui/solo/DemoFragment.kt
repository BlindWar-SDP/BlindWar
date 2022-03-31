package ch.epfl.sdp.blindwar.ui.solo

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.domain.game.Game
import ch.epfl.sdp.blindwar.domain.game.GameTutorial
import ch.epfl.sdp.blindwar.domain.game.SongMetaData
import ch.epfl.sdp.blindwar.domain.game.Tutorial
import ch.epfl.sdp.blindwar.ui.tutorial.GameSummaryFragment
import ch.epfl.sdp.blindwar.ui.tutorial.SongSummaryFragment

open class DemoFragment: Fragment() {
    // Game view model to pass to the next round
    lateinit var game: Game
    protected var playing = true
    protected lateinit var guessEditText: EditText
    protected lateinit var scoreTextView: TextView
    protected lateinit var songMetaData: SongMetaData
    protected lateinit var guessButton: ImageButton
    protected lateinit var countDown: TextView
    protected var duration: Int = 0
    protected lateinit var timer: CountDownTimer

    private lateinit var gameSummary: GameSummaryFragment
    private val gameInstanceViewModel: GameInstanceViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.activity_demo, container, false)
        /** Set up the interface **/
        // Game instance tutorial
        game = GameTutorial(gameInstanceViewModel.gameInstance.value!!, activity?.assets!!)
        game.init()

        duration = Tutorial.gameInstance
            .gameConfig
            .difficulty
            .timeToFind

        // Create and start countdown
        timer = createCountDown()
        countDown = view.findViewById(R.id.countdown)

        // Cache song image
        //Picasso.get().load(songMetaData.imageUrl)

        // Create game summary
        gameSummary = GameSummaryFragment()

        /** Start the game **/
        game.nextRound()
        game.play()
        songMetaData = game.currentMetadata()!!
        timer.start()

        // Get the widgets
        guessEditText = view.findViewById(R.id.guessEditText)
        guessEditText.hint = songMetaData.artist
        scoreTextView = view.findViewById(R.id.scoreTextView)
        guessButton = view.findViewById<ImageButton>(R.id.guessButtonDemo).also{
            it.setOnClickListener{
                guess()
            }
        }

        return view
    }

    protected fun createCountDown(): CountDownTimer {
        return object : CountDownTimer(duration.toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                duration = millisUntilFinished.toInt()
                //Log.d("DURATION", duration.toString())
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
        setAnimVisibility(code)
    }

    override fun onResume() {
        /** Put the UI logic inside the Play Activity / Put a listener on viewmodel
         * that indicates a round change **/
        super.onResume()
        val songRecord = SongSummaryFragment()
        Log.d("RESUME DEMO", activity?.supportFragmentManager?.fragments?.size!!.toString())
        Log.d("RESUME DEMO", activity?.supportFragmentManager?.fragments?.get(0)?.tag.toString())

        //if (activity?.supportFragmentManager?.fragments?.size!! > 1) {
        if (activity?.supportFragmentManager?.fragments!!.size > 1) {
            Log.d("RESUME DEMO", activity?.supportFragmentManager?.fragments?.get(1)?.tag.toString())
            if (activity?.supportFragmentManager?.fragments?.get(1) is SongSummaryFragment) {
                val songFragment =
                    (activity?.supportFragmentManager?.fragments?.get(1) as SongSummaryFragment)
                val bundle = createBundleSongSummary(songFragment.success())

                duration = Tutorial.gameInstance
                    .gameConfig
                    .difficulty
                    .timeToFind

                bundle.putBoolean("liked", songFragment.liked())
                songRecord.arguments = bundle
                gameSummary.setSongFragment(songRecord)
                //Log.d("RESUMED", activity?.supportFragmentManager?.fragments?.get(1).toString())
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
        }

    }

    fun guess() {
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

    override fun onPause() {
        super.onPause()
        game.pause()
        timer.cancel()
    }

    protected fun launchSongSummary(success: Boolean) {
        //setVisibilityLayout(View.GONE)
        timer.cancel()

        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.addToBackStack("DEMO")
        transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

        val songSummary = SongSummaryFragment()
        songSummary.arguments = createBundleSongSummary(success)

        transaction?.add(R.id.play_container, songSummary, "Song Summary")
        transaction?.commit()
    }

    private fun launchGameSummary() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        //transaction?.addToBackStack(GameSummaryFragment::class.java.name)
        transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

        transaction?.replace(R.id.play_container, gameSummary, "Game Summary")
        transaction?.commit()
    }

    private fun createBundleSongSummary(success: Boolean): Bundle {
        val bundle = Bundle()
        bundle.putString("artist", songMetaData.artist)
        bundle.putString("title", songMetaData.title)
        bundle.putString("image", songMetaData.imageUrl)
        bundle.putBoolean("success", success)
        return bundle
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }

    /** Temporary solution : Animation hooks **/
    open fun animNotFound() {}
    open fun setAnimVisibility(code: Int) {}
    open fun pauseAnim() {}
    open fun resumeAnim() {}
}