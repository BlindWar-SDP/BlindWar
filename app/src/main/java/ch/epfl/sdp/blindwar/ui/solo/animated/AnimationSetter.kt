package ch.epfl.sdp.blindwar.ui.solo.animated

import com.airbnb.lottie.LottieAnimationView

object AnimationSetterHelper {
    fun setLikeListener(likeSwitch: Boolean, likeAnimation: LottieAnimationView) {
        if (likeSwitch) {
            likeAnimation.setMinAndMaxFrame(45, 70)
        } else {
            likeAnimation.setMinAndMaxFrame(10, 30)
        }
    }

    fun playLikeAnimation(likeSwitch: Boolean, likeAnimation: LottieAnimationView) {
            if (!likeSwitch) {
                likeAnimation.setMinAndMaxFrame(10, 30)
                likeAnimation.repeatCount = 0
                //likeAnim.speed = 1f
                likeAnimation.playAnimation()
            } else {
                likeAnimation.setMinAndMaxFrame(45, 70)
                likeAnimation.repeatCount = 0
                likeAnimation.playAnimation()
            }
    }
}