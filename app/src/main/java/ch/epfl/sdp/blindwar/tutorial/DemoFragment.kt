package ch.epfl.sdp.blindwar.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.Game
import ch.epfl.sdp.blindwar.game.GameTutorial
import com.gauravk.audiovisualizer.visualizer.BarVisualizer

class DemoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val gameTutorial = GameTutorial(activity?.assets)
        val view: View = inflater.inflate(R.layout.activity_solo, container, false)
        val button: ImageButton = view.findViewById<View>(R.id.startButton) as ImageButton

        button.setOnClickListener{
            gameTutorial.nextRound()
        }

        //get reference to visualizer
        val mVisualizer = view.findViewById<BarVisualizer>(R.id.bar);

        //val audioSessionId: Int = gameTutorial.getSessionId()
        //if (audioSessionId != -1) mVisualizer.setAudioSessionId(audioSessionId)


        return view
    }

    /**
    override fun onDestroy() {
        super.onDestroy()
        view?.findViewById<BarVisualizer>(R.id.bar)?.release()
    }
    **/
}