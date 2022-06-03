package ch.epfl.sdp.blindwar.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.model.config.GameFormat
import ch.epfl.sdp.blindwar.game.multi.MultiPlayerMenuActivity
import ch.epfl.sdp.blindwar.game.util.GameActivity
import ch.epfl.sdp.blindwar.game.util.NetworkConnectivityChecker
import ch.epfl.sdp.blindwar.game.util.Util.loadProfileImage
import ch.epfl.sdp.blindwar.profile.viewmodel.ProfileViewModel

/**
 * Fragment that let the user choose the format of the game (SOLO / MULTI)
 *
 * @constructor creates a PlayMenuFragment
 */
class PlayMenuFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_play, container, false)
        view.findViewById<Button>(R.id.soloBtn).setOnClickListener {
            val intent = Intent(
                requireActivity(),
                GameActivity::class.java
            ).apply { putExtra(GameActivity.GAME_FORMAT_EXTRA_NAME, GameFormat.SOLO) }
            startActivity(intent)
        }
        val btnMulti = view.findViewById<Button>(R.id.multiBtn)
        if (NetworkConnectivityChecker.isOnline()) {
            btnMulti.setOnClickListener {
                if (NetworkConnectivityChecker.isOnline()) {
                    val intent = Intent(requireActivity(), MultiPlayerMenuActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_connexion_internet_unavailable),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            btnMulti.isClickable = false
            btnMulti.alpha = 0.3F
        }

        loadProfileImage(
            profileViewModel.imageRef,
            view.findViewById(R.id.profileView),
            viewLifecycleOwner,
            requireContext()
        )
        return view
    }
}