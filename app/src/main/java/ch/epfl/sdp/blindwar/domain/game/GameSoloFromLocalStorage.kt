package ch.epfl.sdp.blindwar.domain.game

import android.content.ContentResolver
import android.content.res.AssetManager
import androidx.documentfile.provider.DocumentFile


/**
 * Game logic for the tutorial
 *
 * @constructor
 * Create an instance of the tutorial's game logic
 *
 * @param assetManager AssetManager instance to get the mp3 files
 */
class GameSoloFromLocalStorage(
    private val gameInstance: GameInstance,
    private val assetManager: AssetManager,
    private val from: DocumentFile,
    private val contentResolver: ContentResolver
) : Game(gameInstance) {

    override val gameSound = GameSound(assetManager)

    override fun init() {
        gameSound.soundInitFromLocalStorage(this.from, this.contentResolver)
    }
}