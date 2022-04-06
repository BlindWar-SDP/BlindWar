package ch.epfl.sdp.blindwar.domain.game

import android.content.Context
import android.content.res.Resources
import android.media.MediaPlayer
import ch.epfl.sdp.blindwar.data.music.MusicController
import ch.epfl.sdp.blindwar.data.music.MusicMetadata
import ch.epfl.sdp.blindwar.data.music.Playlist
import ch.epfl.sdp.blindwar.ui.solo.PlaylistModel
import java.util.*

class GameSound(val playlist: PlaylistModel, val context: Context, val resources: Resources) {

    // Collection of musics
    private val mediaPlayerPerMusic = MusicController(resources, context).fetchMusics(playlist)

    // Mutable collection of musics
    private var mutableMediaPlayerPerMusic = mediaPlayerPerMusic.toMutableMap()

    // Current metadata
    private var currentMusicMetadata: MusicMetadata? = null

    // Current player
    private var currentMediaPlayer: MediaPlayer? = null

    private fun refreshFetchers() {
        mutableMediaPlayerPerMusic = mediaPlayerPerMusic.toMutableMap()
    }

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

        if (mutableMediaPlayerPerMusic.isEmpty())
            refreshFetchers()

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

        return currentMusicMetadata
    }

    /** Game Sound Controls **/
    /**
     * TODO: Other sound controls : Sound alteration...
     */

    private fun reset() {
        currentMediaPlayer?.reset()
    }

    fun play() {
        currentMediaPlayer?.start()
    }

    fun pause() {
        currentMediaPlayer?.pause()
    }
}