package ch.epfl.sdp.blindwar.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.GlideApp
import ch.epfl.sdp.blindwar.user.UserCache
import ch.epfl.sdp.blindwar.game.multi.MultiPlayerActivity
import ch.epfl.sdp.blindwar.game.solo.SoloActivity
import ch.epfl.sdp.blindwar.profile.viewmodel.ProfileViewModel
import com.google.firebase.storage.StorageReference

/**
 * Fragment that let the user choose the format of the game (SOLO / MULTI)
 *
 * @constructor creates a PlayMenuFragment
 */
class PlayMenuFragment : Fragment(), UserCache {
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_play, container, false)

        view.findViewById<ImageButton>(R.id.soloBtn).setOnClickListener {
            val intent = Intent(requireActivity(), SoloActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<ImageButton>(R.id.multiBtn).setOnClickListener {
            val intent = Intent(requireActivity(), MultiPlayerActivity::class.java)
            startActivity(intent)
        }

        // TODO: Move cache in Repository
        /**
        if (isOffline(activity?.applicationContext!!)) {
            val btn = view.findViewById<ImageButton>(R.id.multiBtn)
            btn.isClickable = false
            btn.alpha = 0.3F
        } **/

        updateProfileImage(profileViewModel.imageRef, view.findViewById(R.id.profileView))

        return view
    }

    /** TODO: Remove duplicated code **/
    private fun updateProfileImage(liveData: LiveData<StorageReference>, imageView: ImageView) {
        liveData.observe(viewLifecycleOwner) {
            if (it.path != "") {
                GlideApp.with(requireActivity())
                    .load(it)
                    .centerCrop()
                    .into(imageView)
            }
        }
    }
}