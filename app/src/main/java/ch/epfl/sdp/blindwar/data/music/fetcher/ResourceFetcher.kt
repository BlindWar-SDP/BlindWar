package ch.epfl.sdp.blindwar.data.music.fetcher

import android.content.Context
import android.content.res.Resources
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import ch.epfl.sdp.blindwar.data.music.MusicImageUrlConstants
import ch.epfl.sdp.blindwar.data.music.MusicMetadata
import ch.epfl.sdp.blindwar.data.music.ResourceMusicMetadata
import java.util.*

class ResourceFetcher(private val context: Context,
                      private val resources: Resources
) : Fetcher {
    override fun fetchMusic(musicMetadata: MusicMetadata): Pair<MusicMetadata, MediaPlayer> {

        // Create a file descriptor to get the author from
        val mediaMetadataRetriever = MediaMetadataRetriever()
        val resourceId = (musicMetadata as ResourceMusicMetadata).resourceId
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
        val baseMetadata = MusicImageUrlConstants.METADATA_TUTORIAL_MUSICS_PER_AUTHOR[author]

        val updateMetadata =
            baseMetadata?.artist?.let {
                ResourceMusicMetadata(
                    baseMetadata.title,
                    it,
                    baseMetadata.imageUrl,
                    baseMetadata.duration,
                    resourceId)
            }
                ?: ResourceMusicMetadata(
                    artist = "",
                    title = "",
                    imageUrl = "",
                    duration = 0,
                    resourceId = musicMetadata.resourceId
                )


        val player = MediaPlayer.create(this.context, musicMetadata.resourceId)

        // Keep the start time low enough so that at least half the song can be heard (for now)
        val time = Random().nextInt(musicMetadata.duration.div(2))

        // Change the current music
        player.seekTo(time)

        // Return a new media player from the give resource id
        return Pair(updateMetadata, player)
    }
}