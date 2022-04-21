package ch.epfl.sdp.blindwar.game.util

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageButton
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.audio.AudioHelper
import ch.epfl.sdp.blindwar.game.solo.fragments.DemoFragment
import ch.epfl.sdp.blindwar.game.model.Playlist
import ch.epfl.sdp.blindwar.game.solo.util.AnimationSetterHelper
import ch.epfl.sdp.blindwar.game.viewmodels.GameInstanceViewModel
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


/**
 * Recycler view adapter for a playlistModel
 *
 * @param playlistSet playlist data
 * @param context Play Activity context
 * @param viewFragment playlist creation view
 * @param gameInstanceViewModel shared viewModel needed to create a game
 */
class PlaylistAdapter(private var playlistSet: ArrayList<Playlist>,
                      private val context: Context,
                      private val viewFragment: View,
                      private val gameInstanceViewModel: GameInstanceViewModel
) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    private val initialPlaylists = ArrayList<Playlist>().apply {
        addAll(playlistSet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.playlist_expanded_cardview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlistSet[position])
    }

    override fun getItemCount(): Int {
        return playlistSet.size
    }

    /**
     * Search filter
     **/
    fun getFilter(): Filter {
        return Util.playlistFilterQuery(initialPlaylists, playlistSet, this)
    }

    /**
     *
     */
    inner class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        /** Playlist info **/
        private val cardView = view.findViewById<ConstraintLayout>(R.id.base_cardview)
        private val name: TextView = view.findViewById(R.id.playlistName)
        private val author = view.findViewById<TextView>(R.id.authorTextview)
        private val coverCard = view.findViewById<CardView>(R.id.coverCard)

        /** Playlist preview **/
        private val progress = view.findViewById<CircularProgressIndicator>(R.id.progress_preview)
        private var playing: Boolean = false
        private var player = MediaPlayer()
        private lateinit var countdown: CountDownTimer
        private val playPreview = view.findViewById<ImageButton>(R.id.playPreview)

        /** Like playlist **/
        private var likeSwitch = false
        private val likeAnimation: LottieAnimationView = view.findViewById(R.id.likeView)

        /** Game info : expanded view **/
        private val expansionView = view.findViewById<ConstraintLayout>(R.id.expanded)
        private val roundPicker: NumberPicker = view.findViewById(R.id.roundPicker)
        private val timerPicker = view.findViewById<NumberPicker>(R.id.timerPicker)
        private val expandButton = view.findViewById<ImageButton>(R.id.expandButton)
        private val playButton = view.findViewById<ImageButton>(R.id.startGame)

        /**
         * Bind the playlistModel to the displayed view
         *
         * @param playlist object to represent
         */
        fun bind(playlist: Playlist) {
            /** Initialize roundPicker **/
            roundPicker.maxValue = playlist.songs.size
            roundPicker.minValue = ROUND_MIN_VALUE
            roundPicker.value = ROUND_DEFAULT_VALUE

            /** Initialize timerPicker **/
            timerPicker.minValue = TIMER_MIN_VALUE
            timerPicker.maxValue = TIMER_MAX_VALUE
            timerPicker.value = TIMER_DEFAULT_VALUE
            timerPicker.displayedValues = ((1 until 10).map{ (5 * it).toString()}).toTypedArray()

            name.text = playlist.name.uppercase()
            author.text = playlist.author

            /** Retrieve the playlist cover : image retrieval must be done on another thread
             *  we use runBlocking to avoid this function to be suspendable **/
            //Picasso.get().load(playlist.imageUrl).into(cover)
            runBlocking {
                withContext(Dispatchers.IO) {
                    try {
                        coverCard.background = BitmapDrawable(Picasso.get().load(playlist.imageUrl).get())
                    } catch (e: Exception) {
                        coverCard.background = AppCompatResources.getDrawable(context, R.drawable.logo)
                    }
                }
            }

            /** Set listeners **/
            setLikeListener()
            setExpansionListener()
            setPreviewListener(playlist)
            setStartGameListener(playlist)
        }

        /** Duplicated function, create Animation object/class setter **/
        private fun setLikeListener() {
            AnimationSetterHelper.setLikeListener(likeSwitch, likeAnimation)

            likeAnimation.setOnClickListener {
                AnimationSetterHelper.playLikeAnimation(likeSwitch, likeAnimation)
                likeSwitch = !likeSwitch
            }
        }

        /**
         * Sets and handle logic of the game start button
         *
         * @param playlist chosen playlist
         */
        private fun setStartGameListener(playlist: Playlist) {
            playButton.setOnClickListener{
                player.pause()
                gameInstanceViewModel.setGamePlaylist(playlist)
                gameInstanceViewModel.setGameTimeRound(
                    timeChosen = (timerPicker.value * 5 + 1) * 1000,
                    roundChosen = roundPicker.value)

                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace((viewFragment.parent as ViewGroup).id,
                             DemoFragment(),
                         "DEMO")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
        }

        /**
         * Sets and handles logic of playlist cardview expansion
         */
        private fun setExpansionListener() {
            cardView.setOnClickListener{
                when (expansionView.visibility) {
                    View.VISIBLE -> {
                        TransitionManager.beginDelayedTransition(
                            cardView,
                            AutoTransition()
                        )
                        expandButton.rotation = -90F
                        expansionView.visibility = View.GONE
                    }

                    else -> {
                        TransitionManager.beginDelayedTransition(
                            cardView,
                            AutoTransition()
                        )
                        expandButton.rotation = 90F
                        expansionView.visibility = View.VISIBLE
                    }
                }
            }
        }

        /**
         * Sets and handles logic of the preview button
         *
         * @param playlist chosen playlist
         */
        private fun setPreviewListener(playlist: Playlist) {
            playPreview.setOnClickListener{

                if (!playing) {
                    player = MediaPlayer()
                    player.setDataSource(playlist.previewUrl)

                    /** Modify the music preview to not spoil the playlist too much **/
                    AudioHelper.soundAlter(player, AudioHelper.HIGH, AudioHelper.FAST)

                    /** Create util object **/
                    val duration = DURATION_FAST
                    countdown = Util.createCountDown(duration, progress)

                    /** Audio player view model or global AudioManager needed **/
                    player.prepare()
                    player.start()
                    countdown.start()
                    progress.visibility = View.VISIBLE

                    /** Set pause image button **/
                    playPreview.setImageResource(R.drawable.ic_baseline_pause_24)

                    /** End preview listener **/
                    player.setOnCompletionListener {
                        progress.visibility = View.INVISIBLE
                        playPreview.setImageResource(R.drawable.play_arrow_small)
                        countdown.cancel()
                    }

                } else {
                    playPreview.setImageResource(R.drawable.play_arrow_small)
                    player.pause()
                    countdown.cancel()
                    progress.progress = 0
                    progress.visibility = View.INVISIBLE
                }

                playing = !playing
            }

        }
    }

    /**
     * Container used to define constant values
     */
    companion object {
        const val ROUND_MIN_VALUE = 1
        const val ROUND_DEFAULT_VALUE = Tutorial.ROUND
        const val TIMER_MIN_VALUE = 1
        const val TIMER_DEFAULT_VALUE = Tutorial.TIME_TO_FIND / 5000
        const val TIMER_MAX_VALUE = 9
        const val DURATION_FAST = 20000L
    }
}


