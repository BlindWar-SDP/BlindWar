package ch.epfl.sdp.blindwar.domain.game

import android.content.ContentResolver
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import androidx.documentfile.provider.DocumentFile
import ch.epfl.sdp.blindwar.data.sound.LocalSoundDataSource
import java.io.FileDescriptor
import java.util.*

class GameSound(private val assetManager: AssetManager) {
    private val mediaMetadataRetriever = MediaMetadataRetriever()
    private val localSoundDataSource = LocalSoundDataSource(assetManager, mediaMetadataRetriever)
    private val player = MediaPlayer()

    // Map each title with its asset file descriptor and its important metadata
    // File descriptor for files in local storage
    private lateinit var assetFileDescriptorAndMetaDataPerTitle: Map<String, Pair<AssetFileDescriptor, SongMetaData>>

    // File descriptor for files in assets folder
    private lateinit var fileDescriptorAndMetaDataPerTitle: Map<String, Pair<FileDescriptor, SongMetaData>>
    private lateinit var playlistNames: MutableSet<String>
    private lateinit var currentMetaData: SongMetaData

    fun soundInitWithSpotifyMetadata() {
        // Convert asset file descriptor to file descriptor
        assetFileDescriptorAndMetaDataPerTitle =
            localSoundDataSource.fetchSoundAssetFileDescriptorsAndMetaDataPerTitleForTutorial()
        playlistNames = refreshPlaylist()
        currentMetaData = SongMetaData("", "", "")
    }

    fun soundInitFromLocalStorage(from: DocumentFile, contentResolver: ContentResolver) {
        val pdf = contentResolver.openFileDescriptor(from.listFiles()[0].uri, "r")
        mediaMetadataRetriever.setDataSource(pdf!!.fileDescriptor)
        val title =
        mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            .toString()
        fileDescriptorAndMetaDataPerTitle =
            from.listFiles().filter { it.isFile }.filter { it.name?.endsWith("mp3") ?: false }
                ?.map { contentResolver.openFileDescriptor(it.uri, "r") }
                ?.associateBy({
                    // Get the title
                    mediaMetadataRetriever.setDataSource(it!!.fileDescriptor)
                    return@associateBy mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                        .toString()
                }, {
                    val author =
                        mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                            .toString()
                    val title =
                        mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                            .toString()

                    return@associateBy Pair(
                        it!!.fileDescriptor,
                        SongMetaData(title, author, "")
                    )
                }) ?: emptyMap()

        playlistNames = refreshPlaylist(true)
        currentMetaData = SongMetaData("", "", "")
    }

    private fun refreshPlaylist(fromLocalStorage: Boolean = false): MutableSet<String> {
        return (if (fromLocalStorage)  fileDescriptorAndMetaDataPerTitle else assetFileDescriptorAndMetaDataPerTitle).keys.toSet() as MutableSet<String>
    }

    fun soundTeardown() {
        pause()
        reset()
    }

    fun getCurrentMetadata(): SongMetaData {
        return currentMetaData
    }

    fun nextRound(fromLocalStorage: Boolean = false): SongMetaData {
        // Stop the music
        pause()
        reset()

        if (playlistNames.isEmpty())
            playlistNames = refreshPlaylist(fromLocalStorage)

        // Get a random title
        val random = Random()
        val title = playlistNames.elementAt(random.nextInt(playlistNames.size))

        // Remove it to the playlist
        playlistNames.remove(title)

        // Get a random time
        if(fromLocalStorage){
            val fileDescriptor = fileDescriptorAndMetaDataPerTitle[title]?.first
            currentMetaData = fileDescriptorAndMetaDataPerTitle[title]?.second!!

            fileDescriptor?.let { player.setDataSource(fileDescriptor) }
            fileDescriptor?.let { mediaMetadataRetriever.setDataSource(fileDescriptor) }
        }
        else {
            val assetFileDescriptor = assetFileDescriptorAndMetaDataPerTitle[title]?.first
            currentMetaData = assetFileDescriptorAndMetaDataPerTitle[title]?.second!!

            assetFileDescriptor?.let { player.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length) }
            assetFileDescriptor?.let { mediaMetadataRetriever.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length) }
        }

        /**
        val time = random.nextInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toInt()
        ?.minus(this.timeToFind) ?: 1)
         **/

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