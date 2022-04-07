package ch.epfl.sdp.blindwar.domain.game

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources


/**
 * Game logic for the tutorial
 *
 * @constructor
 * Create an instance of the tutorial's game logic
 *
 */
class GameTutorial(
    gameInstance: GameInstance,
    context: Context,
    private val resources: Resources
) : Game(gameInstance, context) {

    override fun init() {
        this.musicController = MusicController(
            game.playlist,
            context, resources
        )
        
    }
}