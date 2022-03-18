package ch.epfl.sdp.blindwar.domain.game

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.util.Log
import ch.epfl.sdp.blindwar.domain.game.SongImageUrlConstants.SONG_MAP
import java.util.*
import kotlin.collections.HashMap


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

    // Map each title with its asset file descriptor and its important metadata
    private var assetFileDescriptorAndMetaDataPerTitle: Map<String, Pair<AssetFileDescriptor, SongMetaData>> =
        assetManager.list("")?.filter { it.endsWith(".mp3") }?.map { assetManager.openFd(it) }
            ?.associateBy({
                // Get the title
                mediaMetadataRetriever.setDataSource(it.fileDescriptor, it.startOffset, it.length)
                return@associateBy mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    .toString()
            }, {
                val author = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                    .toString()
                val title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    .toString()

                return@associateBy Pair(
                    it,
                    SongMetaData(
                        title,
                        author,
                        SONG_MAP[author]!!
                    )
                )
            }) ?: emptyMap()

    private var playlist: MutableSet<String> = refreshPlaylist()

    private fun refreshPlaylist(): MutableSet<String> {
        return assetFileDescriptorAndMetaDataPerTitle.keys.toSet() as MutableSet<String>
    }

    override fun nextRound(): SongMetaData? {
        if (playlist.isEmpty())
            playlist = refreshPlaylist()

        // Stop the music
        player.stop()
        player.reset()

        // Get a random title
        val random = Random()
        val title = playlist.elementAt(random.nextInt(playlist.size))
        val afd = assetFileDescriptorAndMetaDataPerTitle[title]?.first
        currentMetaData = assetFileDescriptorAndMetaDataPerTitle[title]?.second!!

        // Remove it to the playlist
        playlist.remove(title)

        // Get a random time
        //Log.d("test", super.timeToFind.toString())
        afd?.let { player.setDataSource(afd.fileDescriptor, afd.startOffset, it.length) }
        afd?.let { mediaMetadataRetriever.setDataSource(afd.fileDescriptor, afd.startOffset, it.length) }
        /**
        val time = random.nextInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toInt()
            ?.minus(this.timeToFind) ?: 1)
        **/

        // Keep the start time low enough so that at least half the song can be heard (for now)
        val time = random.nextInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toInt()
            ?.div(2) ?: 1)

        // Change the current music
        player.prepare()
        player.seekTo(time)

        // Play the music
        player.start()

        return currentMetaData
    }

    override fun guess(titleGuess: String): Boolean {
        return if (titleGuess.uppercase(Locale.getDefault()) == currentMetaData?.title?.uppercase(Locale.getDefault())) {
            score += 1
            true
        } else
            false
    }

    override fun play() {
        player.start()
    }

    override fun pause() {
        player.pause()
    }
}