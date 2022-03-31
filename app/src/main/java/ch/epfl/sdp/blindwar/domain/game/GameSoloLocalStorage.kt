package ch.epfl.sdp.blindwar.domain.game

import android.content.ContentResolver
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import androidx.documentfile.provider.DocumentFile
import java.io.FileDescriptor


/**
 * Game logic for the tutorial
 *
 * @constructor
 * Create an instance of the tutorial's game logic
 *
 * @param assetManager AssetManager instance to get the mp3 files
 */
class GameSoloLocalStorage(
    gameInstance: GameInstance,
    assetManager: AssetManager,
    private val from: DocumentFile,
    contentResolver: ContentResolver
) : Game<FileDescriptor>(gameInstance, assetManager, contentResolver) {
    override fun init() {
        this.gameSound = GameSoundLocalStorage(this.assetManager)
        (this.gameSound as GameSoundLocalStorage).init(this.from, this.contentResolver)
    }
}