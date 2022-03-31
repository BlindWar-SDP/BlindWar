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

class GameSoundLocalStorage(assetManager: AssetManager): GameSound<FileDescriptor>(assetManager) {

    fun init(from: DocumentFile, contentResolver: ContentResolver) {
        this.fileDescriptorAndMetaDataPerTitle = this.localSoundDataSource.fetchSoundFileDescriptorsAndMetaDataPerTitle(from, contentResolver)
        this.playlistNames = refreshPlaylist()
    }

    override fun loadMusicAndMetaDataSource(fileDescriptor: FileDescriptor) {
        this.player.setDataSource(fileDescriptor)
        this.mediaMetadataRetriever.setDataSource(fileDescriptor)
    }
}