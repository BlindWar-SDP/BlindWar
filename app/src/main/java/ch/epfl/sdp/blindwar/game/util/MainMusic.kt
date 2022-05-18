package ch.epfl.sdp.blindwar.game.util

import android.content.Context
import android.media.MediaPlayer
import ch.epfl.sdp.blindwar.R

object MainMusic {
    private const val volume = .4f
    public lateinit var mediaPlayer: MediaPlayer

    fun prepareAndPlay(context: Context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.noisestorm_crab_rave)
        mediaPlayer.isLooping = true
        mediaPlayer.setVolume(volume, volume)
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
        }
    }

    fun play() {
        mediaPlayer.start()
    }

    fun pause() {
        if(mediaPlayer.isPlaying) {
            mediaPlayer.pause();
        }
    }
}