@file:Suppress("KDocUnresolvedReference")

package ch.epfl.sdp.blindwar.data.sound

import android.content.ContentProviderClient
import android.content.ContentResolver
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import ch.epfl.sdp.blindwar.domain.game.SongImageUrlConstants.META_DATA_TUTORIAL_MUSICS_PER_AUTHOR
import ch.epfl.sdp.blindwar.domain.game.SongMetaData
import java.io.File
import java.io.FileDescriptor


class SoundDataSource(
    private val assetManager: AssetManager,
    private val mediaMetadataRetriever: MediaMetadataRetriever
) {

    /**
     * Fetch the sound from a specific path in the local storage
     *
     * @param loadPath Path to load the data from. By default it's equal to the musics tutorial location
     * @return Return a map who maps each title with a pair containing the asset file descriptor and the metadata of the song
     */
    fun fetchSoundFileDescriptorsAndMetaDataPerTitle(
        from: DocumentFile,
        contentResolver: ContentResolver,
    ): Map<String, Pair<FileDescriptor, SongMetaData>> {

        return fetchSoundFileDescriptorsAndSetMetaDataPerTitleFromFileDescriptor(filterDocumentFiles(from.listFiles()).map {
            contentResolver.openFileDescriptor(
                it.uri,
                "r"
            )!!.fileDescriptor
        }) {
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
        contentResolver: ContentResolver
    ): Map<String, Pair<AssetFileDescriptor, SongMetaData>> {
        val tutorialMusicsDirectoryPath = "tutorialMusicsSet"

        assetManager.list(
            tutorialMusicsDirectoryPath
        )?.let { fileNames ->
            filterNames(fileNames).map { tutorialMusicsDirectoryPath.plus(File.separator).plus(it) }
                .map { assetManager.openFd(it) }
        }?.let { assetFileDescriptors ->
            return fetchAssetSoundFileDescriptorsAndSetMetaDataPerTitleFromAssetFileDescriptor(
                assetFileDescriptors
            ) {
                val test = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                    .toString()
                META_DATA_TUTORIAL_MUSICS_PER_AUTHOR[it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                    .toString()]!!
            }
        }

        return emptyMap()
    }

    private fun filterDocumentFiles(documentFiles: Array<DocumentFile>) =
        documentFiles.filter { it.isFile && it.name?.endsWith("mp3") ?: false }

    private fun filterNames(names: Array<String>) = names.filter { it.endsWith("mp3") }

    private fun fetchAssetSoundFileDescriptorsAndSetMetaDataPerTitleFromAssetFileDescriptor(
        assetFileDescriptors: List<AssetFileDescriptor>,
        setMetadataFromRetriever: (mediaMetadataRetriever: MediaMetadataRetriever) -> SongMetaData
    ): Map<String, Pair<AssetFileDescriptor, SongMetaData>> {
        return assetFileDescriptors
            .associateBy({
                // Get the title
                mediaMetadataRetriever.setDataSource(it.fileDescriptor, it.startOffset, it.length)
                return@associateBy mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    .toString()
            }, {
                return@associateBy Pair(
                    it,
                    setMetadataFromRetriever(mediaMetadataRetriever)
                )
            })
    }

    private fun fetchSoundFileDescriptorsAndSetMetaDataPerTitleFromFileDescriptor(
        fileDescriptors: List<FileDescriptor>,
        setMetadataFromRetriever: (mediaMetadataRetriever: MediaMetadataRetriever) -> SongMetaData
    ): Map<String, Pair<FileDescriptor, SongMetaData>> {
        return fileDescriptors
            .associateBy({
                // Get the title
                mediaMetadataRetriever.setDataSource(it)
                val test =
                    mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                        .toString()
                return@associateBy mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    .toString()
            }, {
                return@associateBy Pair(
                    it,
                    setMetadataFromRetriever(mediaMetadataRetriever)
                )
            })
    }
}
