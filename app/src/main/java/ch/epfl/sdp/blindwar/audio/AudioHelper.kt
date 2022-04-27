package ch.epfl.sdp.blindwar.audio

import android.media.MediaPlayer

object AudioHelper {
    const val SLOWED = 0.8F
    const val FAST = 1.5F

    const val LOW = 0.8F
    const val NORMAL = 1F
    const val HIGH = 1.2F

    fun soundAlter(player: MediaPlayer, pitch: Float, speed: Float) {
        val params = player.playbackParams
        params.speed = speed
        params.pitch = pitch
        player.playbackParams = params
    }
}