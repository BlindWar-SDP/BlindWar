package ch.epfl.sdp.blindwar.game.model

import android.content.Context
import android.content.res.Resources
import ch.epfl.sdp.blindwar.audio.MusicViewModel


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
        this.musicViewModel = MusicViewModel(
            game.playlist,
            context, resources
        )
    }
}