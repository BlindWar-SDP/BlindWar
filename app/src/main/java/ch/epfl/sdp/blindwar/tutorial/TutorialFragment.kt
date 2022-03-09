package ch.epfl.sdp.blindwar.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindwar.R
import com.gauravk.audiovisualizer.visualizer.BarVisualizer
import com.gauravk.audiovisualizer.visualizer.BlastVisualizer


class TutorialFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
            val view: View = inflater.inflate(R.layout.fragment_tutorial, container, false)
            val button: Button = view.findViewById<View>(R.id.button) as Button
            button.setOnClickListener{
                val fragment2 = SettingsFragment()
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.tutorial_container, fragment2)
                transaction.commit()
            }

        //get reference to visualizer
        val mVisualizer = view.findViewById<BarVisualizer>(R.id.bar);


        return view
    }
}