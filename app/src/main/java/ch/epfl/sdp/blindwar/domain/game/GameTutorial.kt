package ch.epfl.sdp.blindwar.domain.game

import android.content.res.AssetManager
import java.util.*


/**
 * Game logic for the tutorial
 *
 * @constructor
 * Create an instance of the tutorial's game logic
 *
 * @param assetManager AssetManager instance to get the mp3 files
 */
class GameTutorial(
    private val gameInstance: GameInstance, assetManager: AssetManager,
) : Game(gameInstance, assetManager) {

    override fun init() {
        this.gameSound.soundInitWithSpotifyMetadata(gameInstance.playlist)
    }
}