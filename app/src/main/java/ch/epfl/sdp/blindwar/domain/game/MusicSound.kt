package ch.epfl.sdp.blindwar.domain.game

import android.media.MediaPlayer
import ch.epfl.sdp.blindwar.data.music.MusicController
import ch.epfl.sdp.blindwar.data.music.MusicMetadata
import ch.epfl.sdp.blindwar.data.music.Playlist
import java.util.*

class MusicSound(val playlist: Playlist) {

    // Collection of musics
    private val mediaPlayerPerMusic = MusicController.fetchMusics(playlist)

    // Mutable collection of musics
    private var mutableMediaPlayerPerMusic = MusicController.fetchMusics(playlist).toMutableMap()

    // Current metadata
    private var currentMusicMetadata: MusicMetadata? = null

    // Current player
    private var currentMediaPlayer: MediaPlayer? = null

    init {
        // Fetch the music from the music controller
        MusicController.fetchMusics(playlist)
    }

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

        // Keep the start time low enough so that at least half the song can be heard (for now)
        val time = random.nextInt(musicMetadata.duration.div(2))

        // Set the current metadata and the current player
        currentMusicMetadata = musicMetadata
        currentMediaPlayer = mediaPlayerPerMusic[musicMetadata]

        // Change the current music
        currentMediaPlayer?.seekTo(time)

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