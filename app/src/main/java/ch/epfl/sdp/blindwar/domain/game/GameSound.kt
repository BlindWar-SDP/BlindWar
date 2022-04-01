package ch.epfl.sdp.blindwar.domain.game

import android.content.res.AssetManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import ch.epfl.sdp.blindwar.data.sound.SoundDataSource
import java.util.*

abstract class GameSound<FileDescriptorT>(assetManager: AssetManager) {
    protected val mediaMetadataRetriever = MediaMetadataRetriever()
    protected val localSoundDataSource = SoundDataSource(assetManager, mediaMetadataRetriever)
    protected val player = MediaPlayer()

    // Map each title with its asset file descriptor and its important metadata
    // File descriptor for files in local storage
    //private lateinit var assetFileDescriptorAndMetaDataPerTitle: Map<String, Pair<AssetFileDescriptor, SongMetaData>>

    // File descriptor for files in assets folder
    protected lateinit var fileDescriptorAndMetaDataPerTitle: Map<String, Pair<FileDescriptorT, SongMetaData>>
    protected lateinit var playlistNames: MutableSet<String>
    private var currentMetaData: SongMetaData = SongMetaData("", "", "")

    abstract fun loadMusicAndMetaDataSource(fileDescriptor: FileDescriptorT)

    protected fun refreshPlaylist(): MutableSet<String> {
        return fileDescriptorAndMetaDataPerTitle.keys.toSet() as MutableSet<String>
    }

    fun soundTeardown() {
        pause()
        reset()
    }

    fun getCurrentMetadata(): SongMetaData {
        return currentMetaData
    }

    fun nextRound(): SongMetaData {
        // Stop the music
        pause()
        reset()

        if (playlistNames.isEmpty())
            playlistNames = refreshPlaylist()

        // Get a random title
        val random = Random()
        val title = playlistNames.elementAt(random.nextInt(playlistNames.size))

        // Remove it to the playlist
        playlistNames.remove(title)


        val fileDescriptor = fileDescriptorAndMetaDataPerTitle[title]?.first
        currentMetaData = fileDescriptorAndMetaDataPerTitle[title]?.second!!
        fileDescriptor?.let { this.loadMusicAndMetaDataSource(it) }

        // Keep the start time low enough so that at least half the song can be heard (for now)
        val time = random.nextInt(
            mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                ?.toInt()
                ?.div(2) ?: 1
        )

        // Change the current music
        player.prepare()
        player.seekTo(time)

        // Play the music
        player.start()

        return currentMetaData
    }

    /** Game Sound Controls **/
    /**
     * TODO: Other sound controls : Sound alteration...
     */

    private fun reset() {
        player.reset()
    }

    fun play() {
        player.start()
    }

    fun pause() {
        player.pause()
    }
}