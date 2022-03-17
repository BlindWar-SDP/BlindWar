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
class GameTutorial(private val assetManager: AssetManager, timeToFind: Int) : Game(timeToFind) {
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

        // Get a random time
        afd?.let { player.setDataSource(afd.fileDescriptor, afd.startOffset, it.length) }
        afd?.let { mediaMetadataRetriever.setDataSource(afd.fileDescriptor, afd.startOffset, it.length) }
        val time = random.nextInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toInt()
            ?.minus(this.timeToFind) ?: 1)

        // Change the current music
        this.title = title
        this.player.prepare()
        this.player.seekTo(time)

        // Play the music
        this.player.start()

        return this.assetFileDescriptorAndMetaDataPerTitle[title]?.second
    }


    override fun guess(titleGuess: String): Boolean {
        return if (titleGuess.uppercase(Locale.getDefault()) == this.title?.uppercase(Locale.getDefault())) {
            this.score += 1
            true
        } else
            false
    }

    override fun play() {
        this.player.start()
    }

    override fun pause() {
        this.player.pause()
    }
}