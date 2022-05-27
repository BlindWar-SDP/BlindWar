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

        var musicMetadata: MusicMetadata

        do {

            // Get a random title
            val random = Random().nextInt(mutableMediaPlayerPerMusic.size)
            musicMetadata =
                mutableMediaPlayerPerMusic.keys.elementAt(random)

            val player =
                mutableMediaPlayerPerMusic.values.elementAt(random)

            Log.d("ZAMBO ANGUISSA", musicMetadata.artist)
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