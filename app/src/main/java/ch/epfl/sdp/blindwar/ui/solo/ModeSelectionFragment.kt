package ch.epfl.sdp.blindwar.ui.solo

import android.os.Bundle
import android.util.Log
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
import ch.epfl.sdp.blindwar.domain.game.GameMode
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable

open class ModeSelectionFragment: Fragment() {

    private val gameInstanceViewModel: GameInstanceViewModel by activityViewModels()
    protected lateinit var regularButton: Button
    protected lateinit var survivalButton: Button
    protected lateinit var raceButton: Button
    protected lateinit var funnyButton: CheckBox

    protected lateinit var backButton: ImageButton

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
                for (animation in animations) {
                    if (checked)
                        animation.speed = 2.0f
                    else
                        animation.speed = 0.75f
                }

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

        funnyButton = funnyCheck

        return view
    }

    open fun selectMode(button: View) {
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

    open fun launchPlaylistSelection() {
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
}