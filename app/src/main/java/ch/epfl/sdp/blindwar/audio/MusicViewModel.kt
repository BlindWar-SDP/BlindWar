package ch.epfl.sdp.blindwar.audio

import android.content.Context
import android.content.res.Resources
import android.media.MediaPlayer
import android.util.Log
import ch.epfl.sdp.blindwar.data.music.MusicRepository
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata
import ch.epfl.sdp.blindwar.game.model.Playlist
import java.util.*

class MusicViewModel(playlist: Playlist, context: Context, resources: Resources) {

    // Collection of musics
    private val mediaPlayerPerMusic = MusicRepository(resources, context).fetchMusics(playlist)

    // Mutable collection of musics
    private var mutableMediaPlayerPerMusic = mediaPlayerPerMusic.toMutableMap()

    // Current metadata
    private var currentMusicMetadata: MusicMetadata? = null

    // Current player
    private var currentMediaPlayer: MediaPlayer? = null

    /**
     * Pause and reset the mediaplayer
     *
     */
    fun soundTeardown() {
        pause()
        reset()
    }

    /**
     * return the current MusicMetadata
     *
     * @return
     */
    fun getCurrentMetadata(): MusicMetadata? {
        return currentMusicMetadata
    }

    /**
     * Launch next round of music
     *
     * @return
     */
    fun nextRound(): MusicMetadata? {
        // Stop the music
        pause()

        var musicMetadata: MusicMetadata

        do {
            // Get a random title
            val random = Random().nextInt(mutableMediaPlayerPerMusic.size)
            musicMetadata =
                mutableMediaPlayerPerMusic.keys.elementAt(random)

            val player =
                mutableMediaPlayerPerMusic.values.elementAt(random)

            Log.d("ZAMBO ANGUISSA", musicMetadata.author)
            val ready = player.ready.value!!

            // Player's duration is -1 until the media is ready to be played
        } while (!ready)

        // Remove it to the playlist
        mutableMediaPlayerPerMusic.remove(musicMetadata)


        // Set the current metadata and the current player
        currentMusicMetadata = musicMetadata
        currentMediaPlayer = mediaPlayerPerMusic[musicMetadata]?.mediaPlayer

        // Play the music
        currentMediaPlayer?.start()
        currentMediaPlayer?.isLooping = true

        return currentMusicMetadata
    }

    // GAME SOUND CONTROLS

    /**
     * Reset the mediaplayer
     *
     */
    private fun reset() {
        currentMediaPlayer?.reset()
    }

    /**
     * play the mediaplayer
     *
     */
    fun play() {
        currentMediaPlayer?.start()
    }

    /**
     * Pause the mediaplayer
     *
     */
    fun pause() {
        currentMediaPlayer?.pause()
    }

    /**
     * alter the music by slowing it and low pitching it
     *
     */
    fun summaryMode() {
        AudioHelper.soundAlter(currentMediaPlayer!!, AudioHelper.SLOWED, AudioHelper.LOW)
    }

    /**
     * Alter the music to normal
     *
     */
    fun normalMode() {
        AudioHelper.soundAlter(currentMediaPlayer!!, AudioHelper.NORMAL, AudioHelper.NORMAL)
    }
}