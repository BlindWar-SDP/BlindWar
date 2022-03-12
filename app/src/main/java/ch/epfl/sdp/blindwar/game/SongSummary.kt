package ch.epfl.sdp.blindwar.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindwar.R
import com.chibde.visualizer.LineVisualizer

class SongSummary: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View =
            inflater!!.inflate(R.layout.fragment_demonstration_tutorial, container, false)

        return view
    }
}