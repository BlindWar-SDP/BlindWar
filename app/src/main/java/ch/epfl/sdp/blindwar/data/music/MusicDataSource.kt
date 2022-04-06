/*
package ch.epfl.sdp.blindwar.data.music

import android.content.ContentResolver
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaMetadataRetriever
import androidx.documentfile.provider.DocumentFile
import ch.epfl.sdp.blindwar.data.music.MusicImageUrlConstants.METADATA_TUTORIAL_MUSICS_PER_AUTHOR
import java.io.File
import java.io.FileDescriptor


class MusicDataSource(
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
    ): Map<String, Pair<FileDescriptor, MusicMetadata>> {

        return fetchSoundFileDescriptorsAndSetMetaDataPerTitleFromFileDescriptor(filterDocumentFiles(from.listFiles()).map {
            contentResolver.openFileDescriptor(
                it.uri,
                "r"
            )!!.fileDescriptor
        }) {
            MusicMetadata(
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
    ): Map<String, Pair<AssetFileDescriptor, MusicMetadata>> {
        val tutorialMusicsDirectoryPath = "raw/tutorialMusicsSet"

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
                METADATA_TUTORIAL_MUSICS_PER_AUTHOR[it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
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
        setMetadataFromRetriever: (mediaMetadataRetriever: MediaMetadataRetriever) -> MusicMetadata
    ): Map<String, Pair<AssetFileDescriptor, MusicMetadata>> {
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
        setMetadataFromRetriever: (mediaMetadataRetriever: MediaMetadataRetriever) -> MusicMetadata
    ): Map<String, Pair<FileDescriptor, MusicMetadata>> {
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
*/