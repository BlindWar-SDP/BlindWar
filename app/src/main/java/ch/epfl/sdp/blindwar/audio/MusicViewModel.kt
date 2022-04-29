package ch.epfl.sdp.blindwar.audio

import android.content.Context
import android.content.res.Resources
import android.media.MediaPlayer
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
    private fun refreshFetchers() {
    mutableMediaPlayerPerMusic = mediaPlayerPerMusic.toMutableMap()
    }
     **/

    fun soundTeardown() {
        pause()
        reset()
    }

    fun getCurrentMetadata(): MusicMetadata? {
        return currentMusicMetadata
    }

    fun nextRound(): MusicMetadata? {
        // Stop the music
        pause()
        reset()

        /*
        if (mutableMediaPlayerPerMusic.isEmpty())
        refreshFetchers()
         */

        // Get a random title
        val random = Random()
        val musicMetadata =
            mutableMediaPlayerPerMusic.keys.elementAt(random.nextInt(mutableMediaPlayerPerMusic.size))

        // Remove it to the playlist
        mutableMediaPlayerPerMusic.remove(musicMetadata)


        // Set the current metadata and the current player
        currentMusicMetadata = musicMetadata
        currentMediaPlayer = mediaPlayerPerMusic[musicMetadata]


        // Play the music
        currentMediaPlayer?.start()
        currentMediaPlayer?.isLooping = true

        return currentMusicMetadata
    }

    // GAME SOUND CONTROLS

    private fun reset() {
        currentMediaPlayer?.reset()
    }

    fun play() {
        currentMediaPlayer?.start()
    }

    fun pause() {
        currentMediaPlayer?.pause()
    }

    fun summaryMode() {
        AudioHelper.soundAlter(currentMediaPlayer!!, AudioHelper.SLOWED, AudioHelper.LOW)
    }

    fun normalMode() {
        AudioHelper.soundAlter(currentMediaPlayer!!, AudioHelper.NORMAL, AudioHelper.NORMAL)
    }
}