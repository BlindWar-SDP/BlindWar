package ch.epfl.sdp.blindwar.game

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.util.Log


/**
 * Game logic for the tutorial
 *
 * @constructor
 * Create an instance of the tutorial's game logic
 *
 * @param assetManager AssetManager instance to get the mp3 files
 */
class GameTutorial(assetManager: AssetManager): Game() {
    private val assetManager = assetManager
    private val mediaMetadataRetriever: MediaMetadataRetriever = MediaMetadataRetriever()

    override fun nextRound() {
        val afd: AssetFileDescriptor = assetManager.openFd("Gorillaz-Feel Good Inc.mp3")
        assetManager.list("")?.filter { it.endsWith(".mp3") } ?.forEach { Log.d("MUSIC", it.toString()) }

        val player = MediaPlayer()
        player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        player.prepare()
        player.start()

        mediaMetadataRetriever.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)

        // Music are taken from our assets directory: in this directory, title metadata are well defined for all musics
        super.title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE).toString()

        Log.d("TITLE", super.title)
    }


    override fun guess(titleGuess: String) {
        TODO("Not yet implemented")
    }
}