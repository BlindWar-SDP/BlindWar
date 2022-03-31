package ch.epfl.sdp.blindwar.domain.game

import android.content.ContentResolver
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager


/**
 * Game logic for the tutorial
 *
 * @constructor
 * Create an instance of the tutorial's game logic
 *
 * @param assetManager AssetManager instance to get the mp3 files
 */
class GameTutorial(
    gameInstance: GameInstance, assetManager: AssetManager, contentResolver: ContentResolver
) : Game<AssetFileDescriptor>(gameInstance, assetManager, contentResolver) {

    override fun init() {
        this.gameSound = GameSoundTutorial(assetManager, this.contentResolver)
        (this.gameSound as GameSoundTutorial).init()
    }
}