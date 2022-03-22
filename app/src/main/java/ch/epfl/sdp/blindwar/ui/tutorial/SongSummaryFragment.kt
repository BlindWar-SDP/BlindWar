package ch.epfl.sdp.blindwar.ui.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.SoundEffectConstants
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.ui.viewmodel.SongMetadataViewModel
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.squareup.picasso.Picasso
import kotlin.properties.Delegates

class SongSummaryFragment: Fragment() {
    private lateinit var likeAnim: LottieAnimationView
    private lateinit var confAnim: LottieAnimationView
    private var likeSwitch: Boolean = false
    private val viewModel: SongMetadataViewModel by activityViewModels()

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

        likeAnim = view.findViewById(R.id.likeView)
        setLikeListener()

        confAnim = view.findViewById(R.id.particleView)
        likeSwitch = false

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

        artistText.text = viewModel.selectedMetadata.value?.artist
        trackText.text = viewModel.selectedMetadata.value?.title
        Picasso.get().load(viewModel.selectedMetadata.value?.imageUrl).into(artistView)

        return view
    }

    private fun setLikeListener() {
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
}