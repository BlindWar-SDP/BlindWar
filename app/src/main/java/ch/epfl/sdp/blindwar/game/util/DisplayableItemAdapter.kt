package ch.epfl.sdp.blindwar.game.util

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
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
import ch.epfl.sdp.blindwar.database.MatchDatabase
import ch.epfl.sdp.blindwar.game.model.Displayable
import ch.epfl.sdp.blindwar.game.model.Playlist
import ch.epfl.sdp.blindwar.game.model.config.GameFormat
import ch.epfl.sdp.blindwar.game.model.config.GameMode
import ch.epfl.sdp.blindwar.game.multi.model.Match
import ch.epfl.sdp.blindwar.game.solo.fragments.DemoFragment
import ch.epfl.sdp.blindwar.game.viewmodels.GameInstanceViewModel
import ch.epfl.sdp.blindwar.profile.viewmodel.ProfileViewModel
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


/**
 * Recycler view adapter for a playlistModel
 *
 * @param displayableList playlist data
 * @param context Play Activity context
 * @param viewFragment playlist creation view
 * @param gameInstanceViewModel shared viewModel needed to create a game
 */
class DisplayableItemAdapter(
    private var displayableList: ArrayList<Displayable>,
    private val context: Context,
    private val viewFragment: View,
    private val gameInstanceViewModel: GameInstanceViewModel,
    private val profileViewModel: ProfileViewModel
) :
    RecyclerView.Adapter<DisplayableItemAdapter.DisplayableItemViewHolder>() {

    private val initialItems = ArrayList<Displayable>().apply {
        addAll(displayableList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisplayableItemViewHolder {
        return DisplayableItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.playlist_expanded_cardview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DisplayableItemViewHolder, position: Int) {
        holder.bind(displayableList[position])
    }

    override fun getItemCount(): Int {
        return displayableList.size
    }

    /**
     * Search filter
     **/
    fun getFilter(): Filter {
        return Util.playlistFilterQuery(initialItems, displayableList, this)
    }

    /**
     *
     */
    inner class DisplayableItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
        private val roundTextView = view.findViewById<TextView>(R.id.roundTextView)

        /**
         * Bind the playlistModel to the displayed view
         *
         * @param displayed object to represent
         */
        fun bind(displayed: Displayable) {
            name.text = displayed.getName().uppercase()
            author.text = displayed.getAuthor()

            /** Retrieve the playlist cover : image retrieval must be done on another thread
             *  we use runBlocking to avoid this function to be suspendable **/
            //Picasso.get().load(playlist.imageUrl).into(cover)
            runBlocking {
                withContext(Dispatchers.IO) {
                    try {
                        coverCard.background =
                            BitmapDrawable(Picasso.get().load(displayed.getCover()).get())
                    } catch (e: Exception) {
                        coverCard.background =
                            AppCompatResources.getDrawable(context, R.drawable.logo)
                    }
                }
            }

            /** Set listeners **/
            setLikeListener()
            setPreviewListener(displayed)

            /** Expandable type **/
            if (displayed.extendable()) {
                expandButton.visibility = View.VISIBLE
                setExpansionListener()

                /** Initialize roundPicker **/
                roundPicker.maxValue = displayed.getSize()
                roundPicker.minValue = ROUND_MIN_VALUE
                roundPicker.value = ROUND_DEFAULT_VALUE

                setStartGameListener(displayed as Playlist)

                when (gameInstanceViewModel.gameInstance.value!!.gameConfig.mode) {
                    GameMode.SURVIVAL -> roundTextView.text = context.getString(R.string.lives)
                    else -> roundTextView.text = context.getString(R.string.rounds)
                }

                /** Initialize timerPicker **/
                timerPicker.minValue = TIMER_MIN_VALUE
                timerPicker.maxValue = TIMER_MAX_VALUE
                timerPicker.value = TIMER_DEFAULT_VALUE
                timerPicker.displayedValues =
                    ((1 until 10).map { (5 * it).toString() }).toTypedArray()
            }
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
            playButton.setOnClickListener {
                player.pause()
                gameInstanceViewModel.setGameParameters(
                    timeChosen = (timerPicker.value * 5 + 1) * 1000,
                    roundChosen = roundPicker.value,
                    playlist = playlist
                )

                // Separate solo logic from multiplayer one
                when (gameInstanceViewModel.gameInstance.value?.gameFormat) {
                    GameFormat.SOLO -> {
                        (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                            .replace(
                                (viewFragment.parent as ViewGroup).id,
                                DemoFragment(),
                                "DEMO"
                            )
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit()
                    }
                    GameFormat.MULTI -> {
                        // TODO : Use the new mutable live data to respect the view model architecture
                        // TODO : what ?
                        // Create the match object
                        var match: Match = gameInstanceViewModel.createMatch()
                        val dialog = setProgressDialog(
                            context.getString(R.string.multi_wait_players),
                            match.uid
                        )
                        dialog.show()
                        Firebase.firestore.collection(MatchDatabase.COLLECTION_PATH)
                            .document(match.uid).addSnapshotListener { snapshot, e ->
                                if (e != null) {
                                    return@addSnapshotListener
                                }

                                if (snapshot != null && snapshot.exists()) {
                                    match = snapshot.toObject(Match::class.java)!!
                                    val nbPlayers = match.listPlayers!!.size
                                    if ((match.isPrivate && nbPlayers == match.maxPlayer) ||                //private match wait for all players to join
                                        (!match.isPrivate && nbPlayers > (0.75 * match.maxPlayer).toInt())  //public match wait for 3/4 of the max players number
                                    ) {
                                        //launch game //TODO realtime update
                                        dialog.hide()
                                        (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                                            .replace(
                                                (viewFragment.parent as ViewGroup).id,
                                                DemoFragment(),
                                                "DEMO"
                                            )
                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                            .commit()
                                    } else {
                                        dialog.findViewById<TextView>(R.id.textView_multi_loading)?.text =
                                            context.getString(R.string.multi_wait_players_nb)
                                                .format(nbPlayers, match.maxPlayer)
                                    }
                                }

                            }
                        /*(context as AppCompatActivity).supportFragmentManager.beginTransaction()
                            .replace(
                                (viewFragment.parent as ViewGroup).id,
                                DemoFragment(),
                                "DEMO"
                            )
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit()*/
                    }
                }
            }
        }

        /**
         * display progressDialog cancelable for any messages
         * TODO generify for multiMenu
         * @param message
         */
        private fun setProgressDialog(message: String, matchUID: String): AlertDialog {
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            builder.setNeutralButton(
                context.getString(R.string.cancel_btn)
            ) { it, _ -> it.cancel() }
            builder.setOnCancelListener {
                Toast.makeText(
                    context,
                    context.getString(R.string.toast_canceled_connexion),
                    Toast.LENGTH_SHORT
                ).show()
                MatchDatabase.removeMatch(matchUID, Firebase.firestore)
            }
            val view = View.inflate(context, R.layout.fragment_dialog_loading, null)
            builder.setView(view)
            (view.findViewById<TextView>(R.id.textView_multi_loading)).text = message
            return builder.create()
        }

        /**
         * Sets and handles logic of playlist cardview expansion
         */
        private fun setExpansionListener() {
            cardView.setOnClickListener {
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
         * @param displayed chosen displayed item
         */
        private fun setPreviewListener(displayed: Displayable) {
            playPreview.setOnClickListener {
                if (!playing) {
                    player = MediaPlayer()
                    player.setDataSource(displayed.getPreviewUrl())

                    var duration = DURATION_DEFAULT

                    /** Modify the music preview to not spoil the playlist too much **/
                    if (displayed.extendable()) {
                        AudioHelper.soundAlter(
                            player,
                            AudioHelper.HIGH,
                            AudioHelper.FAST
                        )
                        duration = DURATION_FAST
                    }

                    /** Create util object **/
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
        const val ROUND_DEFAULT_VALUE = GameUtil.ROUND
        const val TIMER_MIN_VALUE = 1
        const val TIMER_DEFAULT_VALUE = GameUtil.TIME_TO_FIND / 5000
        const val TIMER_MAX_VALUE = 9
        const val DURATION_FAST = 20000L
        const val DURATION_DEFAULT = 30000L
    }
}


