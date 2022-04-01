package ch.epfl.sdp.blindwar.data.music.fetcher

import android.R
import android.content.Context
import android.content.res.Resources
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import ch.epfl.sdp.blindwar.data.music.MusicImageUrlConstants.METADATA_TUTORIAL_MUSICS_PER_AUTHOR
import ch.epfl.sdp.blindwar.data.music.MusicMetadata
import java.io.FileDescriptor


class ResourceFetcher(
    context: Context,
    private val resourceId: Int,
    private val resources: Resources
) : Fetcher(context) {
    override fun fetch() {
        // Create a file descriptor to get the author from
        val mediaMetadataRetriever = MediaMetadataRetriever()
        val assetFileDescriptor = resources.openRawResourceFd(resourceId)
        mediaMetadataRetriever.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)


        val author = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            .toString()

        // Create a new media player from the give resource id
        this.mediaPlayer = MediaPlayer.create(this.context, resourceId)

        // Set the metadata
        this.musicMetadata = METADATA_TUTORIAL_MUSICS_PER_AUTHOR[author] ?: MusicMetadata("", "", null, 0)
    }
}