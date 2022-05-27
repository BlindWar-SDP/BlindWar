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

    /**
     * Play the music
     *
     * @return Return true if successful
     */
    fun play(): Boolean {
        if(mediaPlayer != null) {
            mediaPlayer?.start()
            return false
        }
        else {
            return false
        }
    }

    /**
     * Pause the music
     *
     * @return Return true if successful
     */
    fun pause(): Boolean {
        if(mediaPlayer != null) {
            if(mediaPlayer?.isPlaying == true)
                mediaPlayer?.pause()

            return true
        }
        else {
            return false
        }
    }

    fun reset() {
        mediaPlayer?.reset()
        mediaPlayer = null
    }
}