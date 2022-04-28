package ch.epfl.sdp.blindwar.game.solo.util

import com.airbnb.lottie.LottieAnimationView

object AnimationSetterHelper {
    /**
     * Setter for the like animation
     *
     * @param likeSwitch indicates if the object is already liked or not
     * @param likeAnimation reference of the AnimationView object
     */
    fun setLikeListener(likeSwitch: Boolean, likeAnimation: LottieAnimationView) {
        if (likeSwitch) {
            likeAnimation.setMinAndMaxFrame(45, 70)
        } else {
            likeAnimation.setMinAndMaxFrame(10, 30)
        }
    }

    /**
     * Start the like animation
     *
     * @param likeSwitch indicates if the object is already liked or not
     * @param likeAnimation reference of the AnimationView object
     */
    fun playLikeAnimation(likeSwitch: Boolean, likeAnimation: LottieAnimationView) {
            if (!likeSwitch) {
                likeAnimation.setMinAndMaxFrame(10, 30)
                likeAnimation.repeatCount = 0
                likeAnimation.playAnimation()
            } else {
                likeAnimation.setMinAndMaxFrame(45, 70)
                likeAnimation.repeatCount = 0
                likeAnimation.playAnimation()
            }
    }
}