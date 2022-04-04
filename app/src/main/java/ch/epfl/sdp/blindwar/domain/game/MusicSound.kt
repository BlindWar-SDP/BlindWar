package ch.epfl.sdp.blindwar.domain.game

import android.media.MediaPlayer
import ch.epfl.sdp.blindwar.data.music.MusicController
import ch.epfl.sdp.blindwar.data.music.MusicMetadata
import ch.epfl.sdp.blindwar.data.music.Playlist
import java.util.*

class MusicSound(val playlist: Playlist) {

    // Mutable collection of musics
    private var mutableFetchers = playlist.fetchers.toMutableList()

    // Current metadata
    private var currentMusicMetadata: MusicMetadata? = null

    // Current player
    private var currentMediaPlayer: MediaPlayer? = null

    init {
        // Fetch the music from the music controller
        MusicController.fetchMusics(playlist)
    }

    private fun refreshFetchers() {
        mutableFetchers = playlist.fetchers.toMutableList()
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

        if (mutableFetchers.isEmpty())
            refreshFetchers()

        // Get a random title
        val random = Random()
        val fetcher = mutableFetchers.elementAt(random.nextInt(mutableFetchers.size))

        // Remove it to the playlist
        mutableFetchers.remove(fetcher)


        // Keep the start time low enough so that at least half the song can be heard (for now)
        val time = random.nextInt(
            fetcher.musicMetadata.duration
                .div(2)
        )

        // Set the current metadata and the current player
        currentMusicMetadata = fetcher.musicMetadata
        currentMediaPlayer = fetcher.mediaPlayer

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