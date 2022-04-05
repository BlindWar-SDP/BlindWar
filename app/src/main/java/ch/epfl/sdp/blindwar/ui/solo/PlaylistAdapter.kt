package ch.epfl.sdp.blindwar.ui.solo

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.ui.solo.animated.AnimatedPlayActivity
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.squareup.picasso.Picasso
import java.util.*


class PlaylistAdapter(private var playlistSet: ArrayList<Playlist>, val context: Context, val viewFragment: View) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    inner class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.playlistName)
        private val cover: ImageView = itemView.findViewById(R.id.coverPlaylist)
        private val progress = itemView.findViewById<CircularProgressIndicator>(R.id.progress_preview)
        private var playing: Boolean = false
        private var player = MediaPlayer()
        private lateinit var countdown: CountDownTimer
        private var likeSwitch = false
        private val roundPicker: NumberPicker = itemView.findViewById(R.id.roundPicker)
        private val likeAnimation: LottieAnimationView = itemView.findViewById(R.id.likeView)

        private val timerPicker = itemView.findViewById<NumberPicker>(R.id.timerPicker)
        private val expandButton = itemView.findViewById<ImageButton>(R.id.expandButton)
        private val cardView = itemView.findViewById<ConstraintLayout>(R.id.base_cardview)
        private val expansionView = itemView.findViewById<ConstraintLayout>(R.id.expanded)
        private val playButton = itemView.findViewById<ImageButton>(R.id.startGame)

        fun bind(playlist: Playlist) {
            roundPicker.maxValue = 10
            roundPicker.minValue = 1
            timerPicker.maxValue = 45
            timerPicker.minValue = 5

            name.text = playlist.name
            Picasso.get().load(playlist.imageUrl).into(cover)

            /** Set listeners **/
            setLikeListener()
            setExpansionListener()
            setPreviewListener(playlist)
            setStartGameListener()
        }

        /** Duplication code, create Animation class with case classes : like, expand, play **/
        private fun setLikeListener() {
            if (likeSwitch) {
                likeAnimation.setMinAndMaxFrame(45, 70)
            } else {
                likeAnimation.setMinAndMaxFrame(10, 30)
            }

            likeAnimation.setOnClickListener {
                if (!likeSwitch) {
                    likeAnimation.setMinAndMaxFrame(10, 30)
                    likeAnimation.repeatCount = 0
                    //likeAnim.speed = 1f
                    likeAnimation.playAnimation()
                } else {
                    likeAnimation.setMinAndMaxFrame(45, 70)
                    likeAnimation.repeatCount = 0
                    likeAnimation.playAnimation()
                }

                likeSwitch = !likeSwitch
            }
        }

        private fun setStartGameListener() {
            playButton.setOnClickListener{
                (context as AnimatedPlayActivity).supportFragmentManager.beginTransaction()
                    .replace((viewFragment.parent as ViewGroup).id, DemoFragment(), "DEMO")
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
                        expansionView.visibility = View.GONE
                    }

                    else -> {
                        TransitionManager.beginDelayedTransition(
                            cardView,
                            AutoTransition()
                        )
                        expansionView.visibility = View.VISIBLE
                    }
                }
            }
        }

        private fun setPreviewListener(playlist: Playlist) {
            cover.setOnClickListener{
                val duration = 15000L

                if (!playing) {
                    player = MediaPlayer()
                    player.setDataSource(playlist.songs[0].imageUrl)
                    cover.setColorFilter(Color.GRAY, PorterDuff.Mode.LIGHTEN)

                    /** Create util object **/
                    countdown = Util.createCountDown(duration, progress)

                    /** Modify the music preview to not spoil too much **/
                    val params = player.playbackParams
                    params.speed = 0.80F
                    params.pitch = 0.75F
                    player.isLooping = true
                    player.playbackParams = params

                    /** Audio player view model needed **/
                    player.prepare()
                    player.start()
                    countdown.start()
                    progress.visibility = View.VISIBLE

                    player.setOnCompletionListener {
                        cover.setColorFilter(0)
                        progress.visibility = View.INVISIBLE
                        countdown.cancel()
                    }

                } else {
                    cover.setColorFilter(0)
                    player.pause()
                    countdown.cancel()
                    progress.progress = 0
                    progress.visibility = View.INVISIBLE
                }

                playing = !playing
            }
        }
    }

    // Create a copy of localityList that is not a clone
    // (so that any changes in localityList aren't reflected in this list)
    val initialPlaylists = ArrayList<Playlist>().apply {
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

    fun getFilter(): Filter {
        return playlistFilter
    }

    private val playlistFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: ArrayList<Playlist> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                initialPlaylists.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().lowercase(Locale.getDefault())
                initialPlaylists.forEach {
                    if (it.name.lowercase(Locale.getDefault()).contains(query)) {
                        filteredList.add(it)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results?.values is ArrayList<*>) {
                playlistSet.clear()
                playlistSet.addAll(results.values as ArrayList<Playlist>)
                notifyDataSetChanged()
            }
        }
    }
}

object Util {
    fun createCountDown(duration: Long, progress: CircularProgressIndicator): CountDownTimer {
        return object : CountDownTimer(duration, 10) {

            override fun onTick(millisUntilFinished: Long) {
                progress.progress = (((duration.toDouble() - millisUntilFinished.toDouble()) / duration.toDouble()) * 100).toInt()
            }

            override fun onFinish() {}
        }
    }
}

