package ch.epfl.sdp.blindwar.ui.tutorial

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindwar.R


class TutorialContinueToDemoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_continue_to_demo, container, false)
        view.findViewById<Button>(R.id.continueDemoButton).setOnClickListener {
            val intent = Intent(this.activity, AnimatedDemoActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}