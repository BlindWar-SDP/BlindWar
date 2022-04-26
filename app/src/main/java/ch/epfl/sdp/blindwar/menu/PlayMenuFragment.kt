package ch.epfl.sdp.blindwar.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.multi.MultiPlayerActivity
import ch.epfl.sdp.blindwar.game.solo.SoloActivity
import ch.epfl.sdp.blindwar.user.UserCache

/**
 * Fragment that let the user choose the format of the game (SOLO / MULTI)
 *
 * @constructor creates a PlayMenuFragment
 */
class PlayMenuFragment : Fragment(), UserCache {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_play, container, false)

        val coverUrl =
            "https://i.scdn.co/image/ab67616d0000b273b2681e1255ee3e61fab1a6f4"

        val coverMulti =
            "https://i.scdn.co/image/ab67616d0000b273dc30583ba717007b00cceb25"

        view.findViewById<ImageButton>(R.id.soloBtn).setOnClickListener {
            val intent = Intent(requireActivity(), SoloActivity::class.java)
            startActivity(intent)
        }
        view.findViewById<ImageButton>(R.id.multiBtn).setOnClickListener {
            val intent = Intent(requireActivity(), MultiPlayerActivity::class.java)
            startActivity(intent)
        }

        if (isOffline(activity?.applicationContext!!)) {
            val btn = view.findViewById<Button>(R.id.multiBtn)
            btn.isClickable = false
            btn.alpha = 0.3F
        }

        /**
        view.findViewById<ImageButton>(R.id.soloBtn).apply {
        Picasso.get().load(coverUrl).into(this)
        this.setColorFilter(Color.rgb(25, 20, 20), PorterDuff.Mode.DARKEN)
        }

        view.findViewById<ImageButton>(R.id.multiBtn).apply {
        Picasso.get().load(coverMulti).into(this)
        this.setColorFilter(Color.rgb(50, 40, 40), PorterDuff.Mode.DARKEN)
        }
         **/

        return view
    }
}