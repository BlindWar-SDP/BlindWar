package ch.epfl.sdp.blindwar.game.util

import android.content.Context
import android.media.MediaPlayer
import ch.epfl.sdp.blindwar.R

object MainMusic {
    private const val volume = .4f
    private var mediaPlayer: MediaPlayer? = null

    /**
     * Create mediaplayer and start music
     *
     * @param context
     */
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
        return if (mediaPlayer != null) {
            mediaPlayer?.start()
            true
        } else {
            false
        }
    }

    /**
     * Pause the music
     *
     * @return Return true if successful
     */
    fun pause(): Boolean {
        return if (mediaPlayer != null) {
            if (mediaPlayer?.isPlaying == true)
                mediaPlayer?.pause()
            true
        } else {
            false
        }
    }

    /**
     * Reset mediaplayer
     */
    fun reset() {
        mediaPlayer?.reset()
        mediaPlayer = null
    }
}