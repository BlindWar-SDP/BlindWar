package ch.epfl.sdp.blindwar.data.sound

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaMetadataRetriever
import androidx.core.content.pm.PermissionInfoCompat
import ch.epfl.sdp.blindwar.domain.game.SongImageUrlConstants.SONG_MAP
import ch.epfl.sdp.blindwar.domain.game.SongMetaData

class LocalSoundDataSource(private val assetManager: AssetManager,
private val mediaMetadataRetriever: MediaMetadataRetriever) {

    fun fetchSoundFileDescriptors(playlist: List<SongMetaData>, fromPath: String = ""): Map<String, Pair<AssetFileDescriptor, SongMetaData>> {
        return assetMatcher(filterAssetsPlaylist(playlist, fromPath))
    }

    private fun filterAssetsPlaylist(playlist: List<SongMetaData>, fromPath: String): List<String>? {
        return assetManager.list(fromPath)?.filter { it.endsWith(".mp3") && playlist.any{s -> (it.contains(s.title) && (it.contains(s.artist)))}}
    }

    private fun assetMatcher(assets: List<String>?): Map<String, Pair<AssetFileDescriptor, SongMetaData>>  {
        return assets?.map { assetManager.openFd(it) }
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
                    SONG_MAP[author]!!
                )
            }) ?: emptyMap()
    }
}