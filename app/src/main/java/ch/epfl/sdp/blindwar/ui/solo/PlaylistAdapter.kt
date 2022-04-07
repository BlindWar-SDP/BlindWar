package ch.epfl.sdp.blindwar.ui.solo

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
import ch.epfl.sdp.blindwar.data.AudioHelper
import ch.epfl.sdp.blindwar.domain.game.Tutorial
import ch.epfl.sdp.blindwar.ui.solo.animated.AnimationSetterHelper
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class PlaylistAdapter(private var playlistModelSet: ArrayList<PlaylistModel>,
                      private val context: Context,
                      private val viewFragment: View,
                      private val gameInstanceViewModel: GameInstanceViewModel) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    private val initialPlaylists = ArrayList<PlaylistModel>().apply {
        addAll(playlistModelSet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.playlist_expanded_cardview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlistModelSet[position])
    }

    override fun getItemCount(): Int {
        return playlistModelSet.size
    }

    /** Search filter **/
    fun getFilter(): Filter {
        return Util.playlistFilterQuery(initialPlaylists, playlistModelSet, this)
    }

    inner class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        /** Playlist infos **/
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

        fun bind(playlistModel: PlaylistModel) {
            /**TODO: Remove magic values **/
            roundPicker.maxValue = playlistModel.songs.size
            roundPicker.minValue = 1
            roundPicker.value = Tutorial.ROUND

            timerPicker.minValue = 1
            timerPicker.maxValue = 9
            timerPicker.value = Tutorial.TIME_TO_FIND / 5000
            timerPicker.displayedValues = ((1 until 10).map{ (5 * it).toString()}).toTypedArray()

            name.text = playlistModel.name.uppercase()
            author.text = playlistModel.author

            /** Retrieve the playlist cover : image retrieval must be done on another thread
             *  we use runBlocking to avoid this function to be suspendable **/
            //Picasso.get().load(playlist.imageUrl).into(cover)
            runBlocking {
                withContext(Dispatchers.IO) {
                    try {
                        coverCard.background = BitmapDrawable(Picasso.get().load(playlistModel.imageUrl).get())
                    } catch (e: Exception) {
                        coverCard.background = AppCompatResources.getDrawable(context, R.drawable.logo)
                    }
                }
            }

            /** Set listeners **/
            setLikeListener()
            setExpansionListener()
            setPreviewListener(playlistModel)
            setStartGameListener(playlistModel)
        }

        /** Duplicated function, create Animation object/class setter **/
        private fun setLikeListener() {
            AnimationSetterHelper.setLikeListener(likeSwitch, likeAnimation)

            likeAnimation.setOnClickListener {
                AnimationSetterHelper.playLikeAnimation(likeSwitch, likeAnimation)
                likeSwitch = !likeSwitch
            }
        }

        private fun setStartGameListener(playlistModel: PlaylistModel) {
            playButton.setOnClickListener{
                player.pause()
                gameInstanceViewModel.setGamePlaylist(playlistModel)
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

        private fun setPreviewListener(playlistModel: PlaylistModel) {
            playPreview.setOnClickListener{

                if (!playing) {
                    player = MediaPlayer()
                    player.setDataSource(playlistModel.previewUrl)

                    /** Modify the music preview to not spoil the playlist too much **/
                    AudioHelper.soundAlter(player, AudioHelper.HIGH, AudioHelper.FAST)

                    /** Create util object **/
                    val duration = 20000L
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
}


