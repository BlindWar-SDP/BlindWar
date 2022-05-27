package ch.epfl.sdp.blindwar.game.util

import android.content.Context
import android.media.MediaPlayer
import ch.epfl.sdp.blindwar.R
import java.lang.IllegalStateException

object MainMusic {
    private const val volume = .4f
    private var mediaPlayer: MediaPlayer? = null

    fun prepareAndPlay(context: Context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.noisestorm_crab_rave)
        mediaPlayer?.isLooping = true
        mediaPlayer?.setVolume(volume, volume)
        mediaPlayer?.setOnPreparedListener {
            mediaPlayer?.start()
        }
    }

    fun play() {
        if (mediaPlayer != null) {
            mediaPlayer?.start()
        } else {
            throw IllegalStateException()
        }
    }

    fun pause() {
        if (mediaPlayer != null) {
            if (mediaPlayer?.isPlaying == true)
                mediaPlayer?.pause()
        } else {
            throw IllegalStateException()
        }
    }

    fun reset() {
        mediaPlayer?.reset()
        mediaPlayer = null
    }
}