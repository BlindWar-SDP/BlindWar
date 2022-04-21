package ch.epfl.sdp.blindwar.game.solo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.model.GameMode
import ch.epfl.sdp.blindwar.game.viewmodels.GameInstanceViewModel
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable

/**
 * Fragment that let the user choose the game mode
 *
 * @constructor creates a ModeSelectionFragment
 */
class ModeSelectionFragment: Fragment() {

    private val gameInstanceViewModel: GameInstanceViewModel by activityViewModels()
    private lateinit var regularButton: Button
    private lateinit var survivalButton: Button
    private lateinit var raceButton: Button
    private lateinit var funnyButton: CheckBox

    private lateinit var backButton: ImageButton

    private lateinit var animations: List<LottieAnimationView>
    private lateinit var funnyCheck: CheckBox
    private lateinit var particles: List<LottieAnimationView>
    private var checked: Boolean = false

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
        val view = inflater.inflate(R.layout.fragment_animated_mode_selection, container, false)
        regularButton = view.findViewById<Button>(R.id.regularButton_).also{selectMode(it)}
        raceButton = view.findViewById<Button>(R.id.raceButton_).also{selectMode(it)}
        survivalButton = view.findViewById<Button>(R.id.survivalButton_).also{selectMode(it)}

        funnyButton = view.findViewById(R.id.checkBox)
        backButton = view.findViewById<ImageButton>(R.id.back_button).also{
            it.setOnClickListener{
                activity?.onBackPressed()
            }
        }

        particles = arrayListOf(view.findViewById(R.id.particles),
            view.findViewById(R.id.particles2),
            view.findViewById(R.id.particles3))

        animations = arrayListOf(view.findViewById(R.id.vinyl), view.findViewById(R.id.vinyl2),
            view.findViewById(R.id.health), view.findViewById(R.id.health2),
            view.findViewById(R.id.chrono), view.findViewById(R.id.chrono2))

        funnyCheck = view.findViewById<CheckBox>(R.id.checkBox).also { checkBox ->
            checkBox.setOnClickListener {
                checked = !checked
                setAnimationsSpeed()
                toggleParticles()
            }
        }

        funnyButton = funnyCheck

        return view
    }

    /**
     * Sets the listener for the game modes buttons =
     */
    private fun selectMode(button: View) {
        button.setOnClickListener{
            gameInstanceViewModel.setGameMode(when(button.id) {
                R.id.raceButton_ -> GameMode.TIMED
                R.id.survivalButton_ -> GameMode.SURVIVAL
                else -> GameMode.REGULAR
            })

            gameInstanceViewModel.setGameFunny(funnyButton.isChecked)
            launchPlaylistSelection()
        }
    }

    /**
     * Launch the playlist selection fragment when the user has chosen its game mode
     */
    private fun launchPlaylistSelection() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace((view?.parent as ViewGroup).id,
                PlaylistSelectionFragment(),
                "PLAYLIST")
            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ?.commit()
    }

    /** TODO: Create the Info fragments
    private fun showInfo(view: View) {
        view.setOnClickListener{
            // Use a Dialog or a new fragment that presents the mode to the User
        }
    }
    **/

    /**
     * Change the animation speed when the funny mode parameter is (un)checked
     */
    private fun setAnimationsSpeed() {
        for (animation in animations) {
            if (checked)
                animation.speed = 2.0f
            else
                animation.speed = 0.75f
        }
    }

    /**
     * Toggle the particles when the funny mode parameter is (un)checked
     */
    private fun toggleParticles() {
        for (particle in particles) {
            if (checked) {
                particle.visibility = View.VISIBLE
                particle.speed = 2.0f
                particle.repeatMode = LottieDrawable.REVERSE
                particle.playAnimation()
            } else {
                particle.visibility = View.INVISIBLE
                particle.repeatMode = LottieDrawable.RESTART
                particle.repeatMode = LottieDrawable.REVERSE
                particle.pauseAnimation()
            }
        }
    }
}