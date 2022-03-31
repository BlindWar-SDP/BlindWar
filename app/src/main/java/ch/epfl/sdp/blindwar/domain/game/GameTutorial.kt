package ch.epfl.sdp.blindwar.domain.game

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
    private val gameInstance: GameInstance,
    private val assetManager: AssetManager
) : Game(gameInstance) {

    override val gameSound = GameSound(assetManager)

    override fun init() {
        gameSound.soundInitWithSpotifyMetadata(gameInstance.playlist)
    }
}