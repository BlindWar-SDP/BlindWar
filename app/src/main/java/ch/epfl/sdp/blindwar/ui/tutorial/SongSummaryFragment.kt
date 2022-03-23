package ch.epfl.sdp.blindwar.ui.tutorial

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SoundEffectConstants
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import ch.epfl.sdp.blindwar.R
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.squareup.picasso.Picasso
import kotlin.properties.Delegates

class SongSummaryFragment : Fragment() {
    private lateinit var likeAnim: LottieAnimationView
    private lateinit var confAnim: LottieAnimationView
    private var likeSwitch: Boolean = false
    private var success: Boolean = false
    //private val viewModel: SongMetadataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View =
            inflater!!.inflate(R.layout.fragment_song_summary, container, false)

        val artistView = view.findViewById<ImageView>(R.id.artistImageView)
        val artistText = view.findViewById<TextView>(R.id.artistTextView)
        val trackText = view.findViewById<TextView>(R.id.trackTextView)

        /** Like animation **/
        likeAnim = view.findViewById(R.id.likeView)

        likeSwitch = if (arguments != null && (arguments?.containsKey("liked")!!)) {
            arguments?.getBoolean("liked")!!
        }  else false

        setLikeListener()

        /** Background color **/
        success = arguments?.get("success") as Boolean
        val background = view.findViewById<ConstraintLayout>(R.id.song_summary_fragment)

        if (success) {
            background.setBackgroundColor(resources.getColor(R.color.success))
        } else {
            background.setBackgroundColor(resources.getColor(R.color.black))
        }

        //confAnim.playAnimation()
        //confAnim.repeatCount = 0

        /**
        artistText.text = arguments?.get("Artist").toString()
        trackText.text = arguments?.get("Title").toString()
        Picasso.get().load(arguments?.get("Image")!!.toString()).into(artistView)
            viewModel.selectedMetadata.observe(viewLifecycleOwner) { it ->
            artistText.text = it.artist
            trackText.text = it.title
            Picasso.get().load(it.imageUrl).into(artistView)
            }
        **/

        /**
        artistText.text = viewModel.selectedMetadata.value?.artist
        trackText.text = viewModel.selectedMetadata.value?.title
        Picasso.get().load(viewModel.selectedMetadata.value?.imageUrl).into(artistView)
        **/

        /** TODO: define constant key Strings **/
        artistText.text = arguments?.get("artist").toString()
        trackText.text = arguments?.get("title").toString()
        Picasso.get().load(arguments?.get("image").toString()).into(artistView)

        return view
    }

    private fun setLikeListener() {
        if (likeSwitch) {
            likeAnim.setMinAndMaxFrame(45, 70)
        } else {
            likeAnim.setMinAndMaxFrame(10, 30)
        }

        likeAnim.setOnClickListener{
            if (!likeSwitch) {
                likeAnim.setMinAndMaxFrame(10, 30)
                likeAnim.repeatCount = 0
                //likeAnim.speed = 1f
                likeAnim.playAnimation()
            }

            else {
                likeAnim.setMinAndMaxFrame(45, 70)
                likeAnim.repeatCount = 0
                likeAnim.playAnimation()
            }

            likeSwitch = !likeSwitch
        }
    }

    fun liked(): Boolean {
        return likeSwitch
    }

    fun success(): Boolean {
        return success
    }
}