package ch.epfl.sdp.blindwar.audio

import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData

/**
 * Wrapper of a MediaPlayer with an indicative ready state
 */
class ReadyMediaPlayer(
    val mediaPlayer: MediaPlayer, // wrapped MediaPlayer
    val ready: MutableLiveData<Boolean> // ready state boolean
)