package ch.epfl.sdp.blindwar.data.music.fetcher

import android.content.Context
import android.content.res.Resources
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import ch.epfl.sdp.blindwar.audio.ReadyMediaPlayer
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata
import ch.epfl.sdp.blindwar.game.util.GameUtil
import java.util.*

class ResourceFetcher(
    private val context: Context,
    private val resources: Resources
) : Fetcher {
    /**
     * Fetch the music from resources
     *
     * @param musicMetadata
     * @return a pair of musicMetadata and ReadyMediaPlayer
     */
    override fun fetchMusic(musicMetadata: MusicMetadata): Pair<MusicMetadata, ReadyMediaPlayer> {

        // Create a file descriptor to get the author from
        val mediaMetadataRetriever = MediaMetadataRetriever()
        val resourceId = musicMetadata.resourceId!!
        val assetFileDescriptor = resources.openRawResourceFd(resourceId)
        mediaMetadataRetriever.setDataSource(
            assetFileDescriptor.fileDescriptor,
            assetFileDescriptor.startOffset,
            assetFileDescriptor.length
        )

        val author =
            mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                .toString()

        // Set the metadata
        val baseMetadata = GameUtil.metadataTutorial()[author]

        val updateMetadata =
            baseMetadata?.author?.let {
                MusicMetadata.createWithResourceId(
                    baseMetadata.name,
                    baseMetadata.author,
                    baseMetadata.cover,
                    baseMetadata.duration,
                    resourceId
                )
            } ?: MusicMetadata.createWithResourceId(
                name = "",
                author = "",
                cover = "",
                duration = 0,
                resourceId = musicMetadata.resourceId!!
            )

        val player = MediaPlayer.create(this.context, musicMetadata.resourceId!!)

        // Keep the start time low enough so that at least half the song can be heard (for now)
        val time = Random().nextInt(musicMetadata.duration.div(2))

        // Change the current music
        player.seekTo(time)

        // Return a new media player from the give resource id
        return Pair(updateMetadata, ReadyMediaPlayer(player, MutableLiveData(true)))
    }
}