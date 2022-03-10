package ch.epfl.sdp.blindwar.game

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import java.util.*


/**
 * Game logic for the tutorial
 *
 * @constructor
 * Create an instance of the tutorial's game logic
 *
 * @param assetManager AssetManager instance to get the mp3 files
 */
class GameTutorial(private val assetManager: AssetManager) : Game() {
    private val mediaMetadataRetriever: MediaMetadataRetriever = MediaMetadataRetriever()
    private val player = MediaPlayer()
    val sessionId: Int
        get() = player.audioSessionId

    // Map each title with its asset file descriptor and its important metadata
    private var assetFileDescriptorAndMetaDataPerTitle: Map<String, Pair<AssetFileDescriptor, MusicMetaData>> =
        assetManager.list("")?.filter { it.endsWith(".mp3") }?.map { assetManager.openFd(it) }
            ?.associateBy({
                // Get the title
                mediaMetadataRetriever.setDataSource(it.fileDescriptor, it.startOffset, it.length)
                return@associateBy mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    .toString()
            }, {
                return@associateBy Pair(
                    it,
                    MusicMetaData(
                        mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                            .toString(),
                        mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                            .toString()
                    )
                )
            }) ?: emptyMap()

    private var playlist: MutableSet<String> = assetFileDescriptorAndMetaDataPerTitle.keys.toSet() as MutableSet<String>

    override fun nextRound(): MusicMetaData? {

        // Stop the music
        player.stop()
        player.reset()

        // Get a random title
        val random = Random()
        val title = this.playlist.elementAt(random.nextInt(this.playlist.size))
        val afd = this.assetFileDescriptorAndMetaDataPerTitle[title]?.first

        // Remove it to the playlist
        this.playlist.remove(title)

        // Change the current music
        this.title = title
        afd?.let { player.setDataSource(afd.fileDescriptor, afd.startOffset, it.length) }
        player.prepare()

        // Play the music
        player.start()

        return this.assetFileDescriptorAndMetaDataPerTitle[title]?.second
    }


    override fun guess(titleGuess: String): Int {
        if (titleGuess == this.title) {
            this.score += 1
            this.nextRound()
        }

        return this.score
    }
}