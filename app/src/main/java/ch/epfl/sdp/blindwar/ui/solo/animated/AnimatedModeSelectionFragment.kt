package ch.epfl.sdp.blindwar.ui.solo.animated

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.fragment.app.FragmentTransaction
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.domain.game.GameMode
import ch.epfl.sdp.blindwar.ui.solo.ModeSelectionFragment
import ch.epfl.sdp.blindwar.ui.solo.PlaylistSelectionFragment
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable.RESTART
import com.airbnb.lottie.LottieDrawable.REVERSE

class AnimatedModeSelectionFragment: ModeSelectionFragment() {

    private lateinit var animations: List<LottieAnimationView>
    private lateinit var funnyCheck: CheckBox
    private lateinit var particles: List<LottieAnimationView>
    private var checked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_animated_mode_selection, container, false)
        super.regularButton = view.findViewById<Button>(R.id.regularButton_).also {selectMode(it)}
        super.survivalButton = view.findViewById<Button>(R.id.survivalButton_).also {selectMode(it)}
        super.raceButton = view.findViewById<Button>(R.id.raceButton_).also{selectMode(it)}
        super.backButton = view.findViewById<ImageButton>(R.id.back_button).also{
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
                        particle.speed = 2.0f
                        particle.repeatMode = REVERSE
                        particle.playAnimation()
                    } else {
                        //particle.cancelAnimation()
                        particle.repeatMode = RESTART
                        particle.repeatMode = REVERSE
                        particle.pauseAnimation()
                    }
                }
            }
        }

        return view
    }

    override fun selectMode(button: View) {
        button.setOnClickListener{
            gameInstance.setGameMode(when(button.id) {
                R.id.raceButton_ -> GameMode.TIMED
                R.id.survivalButton_ -> GameMode.SURVIVAL
                else -> GameMode.REGULAR
            })

            activity?.supportFragmentManager?.beginTransaction()
                ?.replace((view?.parent as ViewGroup).id, AnimatedPlaylistSelectionFragment(), "PLAYLIST")
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                ?.commit()
        }
    }
}