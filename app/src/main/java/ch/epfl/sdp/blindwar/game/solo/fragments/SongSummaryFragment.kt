package ch.epfl.sdp.blindwar.game.solo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata
import ch.epfl.sdp.blindwar.game.util.AnimationSetterHelper
import ch.epfl.sdp.blindwar.profile.viewmodel.ProfileViewModel
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

/**
 * Game over fragment displayed after a round
 *
 * @constructor creates a SongSummaryFragment
 */
class SongSummaryFragment : Fragment() {
    private lateinit var likeAnimation: LottieAnimationView
    private lateinit var skip: ImageButton
    private var likeSwitch: Boolean = false
    private var success: Boolean = false
    private var timerStart: Long = 0
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var title: String
    private lateinit var artist: String
    private lateinit var cover: String
    private lateinit var layout: ConstraintLayout

    private lateinit var artistText: TextView
    private lateinit var trackText: TextView
    private lateinit var artistView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View =
            inflater.inflate(R.layout.fragment_song_summary, container, false)

        /** Metadata **/
        artistView = view.findViewById(R.id.artistImageView)
        artistText = view.findViewById(R.id.artistTextView)
        trackText = view.findViewById(R.id.trackTextView)
        setMetadata()

        /** Background color **/
        success = arguments?.get(SUCCESS_KEY) as Boolean
        layout = view.findViewById(R.id.song_summary_fragment)

        if (success) {
            layout.setBackgroundColor(resources.getColor(R.color.success, activity?.theme))
        } else {
            layout.setBackgroundColor(resources.getColor(R.color.black, activity?.theme))
        }

        /** Like animation **/
        skip = view.findViewById<ImageButton>(R.id.skip_next_summary).also { button ->
            button.setOnClickListener {
                activity?.onBackPressed()
            }
        }
        timerStart = System.currentTimeMillis()
        setLikeAnimation(view)
        setLikeListener()

        return view
    }

    private fun setLikeAnimation(view: View) {
        /** Like animation **/
        likeAnimation = view.findViewById(R.id.likeView)

        likeSwitch = if (arguments != null && (arguments?.containsKey("liked")!!)) {
            skip.visibility = View.GONE
            layout.background =
                if (success)
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.back_frame_success,
                        activity?.theme
                    )
                else
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.back_frame_failure,
                        activity?.theme
                    )
            arguments?.getBoolean("liked")!!
        } else
            false

        setLikeListener()
    }

    private fun setMetadata() {
        artist = arguments?.get(ARTIST_KEY).toString()
        title = arguments?.get(TITLE_KEY).toString()
        cover = arguments?.get(COVER_KEY).toString()

        artistText.text = artist
        trackText.text = title
        Picasso.get().load(cover).into(artistView)
    }

    /**
     * Setter for the like animation listener
     */
    private fun setLikeListener() {
        AnimationSetterHelper.setLikeListener(likeSwitch, likeAnimation)
        likeAnimation.setOnClickListener {
            AnimationSetterHelper.playLikeAnimation(likeSwitch, likeAnimation)
            likeSwitch = !likeSwitch
            val currentUser = FirebaseAuth.getInstance().currentUser

            if (currentUser != null) {
                // Reconstruct MusicMetadata from arguments
                val defaultDuration = 10000
                val defaultUri = ""
                val music =
                    MusicMetadata.createWithURI(title, artist, cover, defaultDuration, defaultUri)
                profileViewModel.likeMusic(music)
            }
        }
    }

    /**
     * Getter for the like attribute
     *
     * @return true if the song is currently liked else false
     */
    fun liked(): Boolean {
        return likeSwitch
    }

    /**
     * Getter for the success attribute
     *
     * @return true if user has guessed the song else false
     */
    fun success(): Boolean {
        return success
    }

    companion object {
        const val ARTIST_KEY = "ARTIST"
        const val TITLE_KEY = "TITLE"
        const val COVER_KEY = "COVER"
        const val SUCCESS_KEY = "SUCCESS"
    }
}