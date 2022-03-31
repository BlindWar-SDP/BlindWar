package ch.epfl.sdp.blindwar.ui.solo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.domain.game.GameMode

open class ModeSelectionFragment: Fragment() {

    private val gameInstance: GameInstanceViewModel by activityViewModels()
    protected lateinit var regularButton: Button
    protected lateinit var survivalButton: Button
    protected lateinit var raceButton: Button

    protected lateinit var backButton: ImageButton

    /** TODO: Create the Info fragments and set listeners
    private lateinit var settingsButton: ImageButton
    private lateinit var infoRegular: ImageButton
    private lateinit var infoSurvival: ImageButton
    private lateinit var infoTimed: ImageButton
    **/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_mode_selection, container, false)
        regularButton = view.findViewById<Button>(R.id.regularButton_).also{selectMode(it)}
        raceButton = view.findViewById<Button>(R.id.raceButton_).also{selectMode(it)}
        survivalButton = view.findViewById<Button>(R.id.survivalButton_).also{selectMode(it)}

        /** TODO: Correct bug in next sprint
        backButton = view.findViewById<ImageButton>(R.id.back_button).also{
            it.setOnClickListener{
                //activity?.onBackPressed()
            }
        }
        **/

        return view
    }


    protected fun selectMode(button: View) {
        button.setOnClickListener{
            gameInstance.setGameMode(when(button.id) {
                R.id.raceButton_ -> GameMode.TIMED
                R.id.survivalButton_ -> GameMode.SURVIVAL
                else -> GameMode.REGULAR
            })

        activity?.supportFragmentManager?.beginTransaction()
            ?.replace((view?.parent as ViewGroup).id, PlaylistSelectionFragment(), "PLAYLIST")
            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ?.commit()
        }
    }

    /** TODO: Create the Info fragments and implement the fun
    private fun showInfo(view: View) {
        view.setOnClickListener{
            // Use a Dialog or a new fragment that presents the mode to the User
        }
    }
    **/
}