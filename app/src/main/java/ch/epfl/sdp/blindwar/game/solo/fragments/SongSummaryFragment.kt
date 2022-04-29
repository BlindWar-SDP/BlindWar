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

import ch.epfl.sdp.blindwar.data.music.metadata.URIMusicMetadata
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
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View =
            inflater.inflate(R.layout.fragment_song_summary, container, false)

        val artistView = view.findViewById<ImageView>(R.id.artistImageView)
        val artistText = view.findViewById<TextView>(R.id.artistTextView)
        val trackText = view.findViewById<TextView>(R.id.trackTextView)

        /** Like animation **/
        likeAnimation = view.findViewById(R.id.likeView)

        likeSwitch = if (arguments != null && (arguments?.containsKey("liked")!!)) {
            arguments?.getBoolean("liked")!!
        } else false

        setLikeListener()

        /** Background color **/
        success = arguments?.get("success") as Boolean
        val layout = view.findViewById<ConstraintLayout>(R.id.song_summary_fragment)

        if (success) {
            layout.setBackgroundColor(resources.getColor(R.color.success, activity?.theme))
        } else {
            layout.setBackgroundColor(resources.getColor(R.color.black, activity?.theme))
        }

        /** Like animation **/
        likeAnimation = view.findViewById(R.id.likeView)

        skip = view.findViewById<ImageButton>(R.id.skip_next_summary).also { button ->
            button.setOnClickListener {
                activity?.onBackPressed()
            }
        }

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

        /** TODO: define constant key Strings **/
        artistText.text = arguments?.get("artist").toString()
        trackText.text = arguments?.get("title").toString()
        Picasso.get().load(arguments?.get("image").toString()).into(artistView)

        return view
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

                // Reconstruct musicmetadata from arguments
                val defaultDuration = 10000
                val defaultUri = ""
                val title: String = arguments?.get("title").toString()
                val artist: String = arguments?.get("artist").toString()
                val image: String = arguments?.get("image").toString()
                val music = URIMusicMetadata(title, artist, image, defaultDuration, defaultUri)
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
}