package ch.epfl.sdp.blindwar.game

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.util.Log
import java.util.*


/**
 * Game logic for the tutorial
 *
 * @constructor
 * Create an instance of the tutorial's game logic
 *
 * @param assetManager AssetManager instance to get the mp3 files
 */
class GameTutorial(assetManager: AssetManager?): Game() {

    private val assetManager = assetManager
    private val mediaMetadataRetriever: MediaMetadataRetriever = MediaMetadataRetriever()
    private val player = MediaPlayer()
    val sessionId: Int
        get() = player.audioSessionId


    // Map each title with its asset file descriptor
    private var assetFileDescriptorPerTitle: Map<String, AssetFileDescriptor> =
        assetManager?.list("")?.filter { it.endsWith(".mp3") } ?.map { assetManager.openFd(it) }?.associateBy({
            // Get the title
            mediaMetadataRetriever.setDataSource(it.fileDescriptor, it.startOffset, it.length)
            return@associateBy mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE).toString()
        }, { it }) ?: emptyMap()

    private var titlePlayed: Set<String> = assetFileDescriptorPerTitle.keys

    override fun nextRound() {

        // Stop the music
        player.stop()

        // Get a random title
        val random = Random()
        val title = this.titlePlayed.elementAt(random.nextInt(this.titlePlayed.size))
        val afd = this.assetFileDescriptorPerTitle[title]

        // Change the current music
        afd?.let { player.setDataSource(afd.fileDescriptor, afd.startOffset, it.length) }
        player.prepare()

        // Play the music
        player.start()
    }


    override fun guess(titleGuess: String) {
        TODO("Not yet implemented")
    }
}