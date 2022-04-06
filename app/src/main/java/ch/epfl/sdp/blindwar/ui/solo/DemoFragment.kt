package ch.epfl.sdp.blindwar.ui.solo

import android.app.Activity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.data.music.MusicMetadata
import ch.epfl.sdp.blindwar.domain.game.GameTutorial
import ch.epfl.sdp.blindwar.domain.game.Tutorial
import ch.epfl.sdp.blindwar.ui.tutorial.GameSummaryFragment
import ch.epfl.sdp.blindwar.ui.tutorial.SongSummaryFragment

open class DemoFragment : Fragment() {
    // Game view model to pass to the next round
    lateinit var game: GameTutorial
    protected var playing = true
    protected lateinit var guessEditText: EditText
    protected lateinit var scoreTextView: TextView
    protected lateinit var musicMetadata: MusicMetadata
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
        game = context?.let {
            GameTutorial(
                gameInstanceViewModel.gameInstance.value!!,
                it,
                resources
            )
        }!!

        game.init()

        duration = gameInstanceViewModel
            .gameInstance
            .value
            ?.gameConfig
            ?.parameter
            ?.timeToFind!!

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
        musicMetadata = game.currentMetadata()!!
        timer.start()

        // Get the widgets
        guessEditText = view.findViewById(R.id.guessEditText)
        guessEditText.hint = musicMetadata.artist
        scoreTextView = view.findViewById(R.id.scoreTextView)
        guessButton = view.findViewById<ImageButton>(R.id.guessButtonDemo).also {
            it.setOnClickListener {
                guess(false, isAuto = false)

                // Delete the text of the guess
                guessEditText.setText("")
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

        if (activity?.supportFragmentManager?.fragments!!.size > 1) {
            if (activity?.supportFragmentManager?.fragments?.get(1) is SongSummaryFragment) {
                val songFragment =
                    (activity?.supportFragmentManager?.fragments?.get(1) as SongSummaryFragment)
                val bundle = createBundleSongSummary(songFragment.success())

                duration = Tutorial.gameInstance
                    .gameConfig
                    .parameter
                    .timeToFind

                bundle.putBoolean("liked", songFragment.liked())
                songRecord.arguments = bundle
                gameSummary.setSongFragment(songRecord)
                if (!game.nextRound()) {
                    setVisibilityLayout(View.VISIBLE)
                    // Pass to the next music
                    musicMetadata = game.currentMetadata()!!
                    guessEditText.hint = musicMetadata.artist
                    guessEditText.setText("")
                    // Cache song image
                    // Picasso.get().load(viewModel.selectedMetadata.value?.imageUrl)
                    timer = createCountDown()
                    timer.start()
                } else {
                    launchGameSummary()
                }
            }
        }
    }

    fun guess(isVocal: Boolean, isAuto: Boolean) {
        if (game.guess(guessEditText.text.toString(), isVocal)) {
            // Update the number of point view
            scoreTextView.text = game.score.toString()
              (activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
              .hideSoftInputFromWindow(view?.windowToken, 0)
            launchSongSummary(success = true)
        } else if (!isAuto) {
            animNotFound()
        }
    }

    override fun onPause() {
        super.onPause()
        game.pause()
        timer.cancel()
    }

    protected fun launchSongSummary(success: Boolean) {
        setVisibilityLayout(View.GONE)
        timer.cancel()

        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.addToBackStack("DEMO")
        transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

        val songSummary = SongSummaryFragment()
        songSummary.arguments = createBundleSongSummary(success)

        transaction?.add((view?.parent as ViewGroup).id, songSummary, "Song Summary")
        transaction?.commit()
    }

    private fun launchGameSummary() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        //transaction?.addToBackStack(GameSummaryFragment::class.java.name)
        transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction?.replace((view?.parent as ViewGroup).id, gameSummary, "Game Summary")
        transaction?.commit()
    }

    private fun createBundleSongSummary(success: Boolean): Bundle {
        val bundle = Bundle()
        bundle.putString("artist", musicMetadata.artist)
        bundle.putString("title", musicMetadata.title)
        bundle.putString("image", musicMetadata.imageUrl)
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