package ch.epfl.sdp.blindwar.data.sound

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaMetadataRetriever
import androidx.core.content.pm.PermissionInfoCompat
import ch.epfl.sdp.blindwar.domain.game.SongImageUrlConstants.SONG_MAP
import ch.epfl.sdp.blindwar.domain.game.SongMetaData
import java.io.FileDescriptor

class LocalSoundDataSource(private val assetManager: AssetManager,
private val mediaMetadataRetriever: MediaMetadataRetriever) {

    fun fetchSoundFileDescriptors(playlist: List<SongMetaData>): Map<String, Pair<FileDescriptor, SongMetaData>> {
        return assetMatcher(filterAssetsPlaylist(playlist))
    }

    private fun filterAssetsPlaylist(playlist: List<SongMetaData>): List<String>? {
        return assetManager.list("")?.filter { it.endsWith(".mp3")}
    }

    private fun assetMatcher(assets: List<String>?): Map<String, Pair<FileDescriptor, SongMetaData>>  {
        return assets?.map { assetManager.openFd(it).fileDescriptor }
            ?.associateBy({
                // Get the title
                mediaMetadataRetriever.setDataSource(it)
                return@associateBy mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    .toString()
            }, {
                val author = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                    .toString()
                val title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    .toString()

                return@associateBy Pair(
                    it,
                    SONG_MAP[author]!!
                )
            }) ?: emptyMap()
    }
}