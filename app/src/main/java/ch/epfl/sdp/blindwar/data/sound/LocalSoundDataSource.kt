package ch.epfl.sdp.blindwar.data.sound

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaMetadataRetriever
import ch.epfl.sdp.blindwar.domain.game.SongImageUrlConstants.META_DATA_TUTORIL_MUSICS_PER_AUTHOR
import ch.epfl.sdp.blindwar.domain.game.SongMetaData

class LocalSoundDataSource(
    private val assetManager: AssetManager,
    private val mediaMetadataRetriever: MediaMetadataRetriever
) {

    /**
     * Fetch the sound from a specific path in the local storage
     *
     * @param metaDataMusics Path to load the data from. By default it's equal to the musics tutorial location
     * @return Return a map who maps each title with a pair containing the asset file descriptor and the metadata of the song
     */
    fun fetchSoundAssetFileDescriptorsAndMetaDataPerTitle(
        loadPath: String
    ): Map<String, Pair<AssetFileDescriptor, SongMetaData>> {
        return fetchSoundAssetFileDescriptorsAndSetMetaDataPerTitle(loadPath) {
            SongMetaData(
                it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    .toString(),
                it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                    .toString()
            )
        }
    }

    /**
     * Fetch the tutorial's musics from the local storage
     *
     * @return Return a map who maps each title with a pair containing the asset file descriptor and the metadata of the song
     */
    fun fetchSoundAssetFileDescriptorsAndMetaDataPerTitleForTutorial(
    ): Map<String, Pair<AssetFileDescriptor, SongMetaData>> {
        return fetchSoundAssetFileDescriptorsAndSetMetaDataPerTitle("tutorialMusicsSet/") {
            META_DATA_TUTORIL_MUSICS_PER_AUTHOR[it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                .toString()]!!
        }
    }

    private fun fetchSoundAssetFileDescriptorsAndSetMetaDataPerTitle(
        loadPath: String,
        setMetadataFromRetriever: (mediaMetadataRetriever: MediaMetadataRetriever) -> SongMetaData
    ): Map<String, Pair<AssetFileDescriptor, SongMetaData>> {
        return assetManager.list(loadPath)?.filter { it.endsWith(".mp3") }
            ?.map { loadPath.plus(it) }?.map { assetManager.openFd(it) }
            ?.associateBy({
                // Get the title
                mediaMetadataRetriever.setDataSource(it.fileDescriptor, it.startOffset, it.length)
                return@associateBy mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    .toString()
            }, {
                return@associateBy Pair(
                    it,
                    setMetadataFromRetriever(mediaMetadataRetriever)
                )
            }) ?: emptyMap()
    }
}