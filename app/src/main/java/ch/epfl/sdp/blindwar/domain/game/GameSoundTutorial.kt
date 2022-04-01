package ch.epfl.sdp.blindwar.domain.game

import android.content.ContentResolver
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import androidx.documentfile.provider.DocumentFile
import ch.epfl.sdp.blindwar.data.sound.SoundDataSource
import java.io.FileDescriptor
import java.util.*

class GameSoundTutorial(assetManager: AssetManager, private val contentResolver: ContentResolver): GameSound<AssetFileDescriptor>(assetManager) {

    fun init() {
        // Convert asset file descriptor to file descriptor
        this.fileDescriptorAndMetaDataPerTitle =
            this.localSoundDataSource.fetchSoundAssetFileDescriptorsAndMetaDataPerTitleForTutorial(this.contentResolver)
        this.playlistNames = refreshPlaylist()
    }

    override fun loadMusicAndMetaDataSource(assetFileDescriptor: AssetFileDescriptor) {
        this.player.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)
        this.mediaMetadataRetriever.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)
    }
}