package ch.epfl.sdp.blindwar.domain.game

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.data.music.Playlist
import ch.epfl.sdp.blindwar.data.music.fetcher.ResourceMusicReference
import ch.epfl.sdp.blindwar.data.music.fetcher.URIMusicReference
import ch.epfl.sdp.blindwar.domain.game.Tutorial.gameInstance
import ch.epfl.sdp.blindwar.ui.solo.PlaylistModel


/**
 * Game logic for the tutorial
 *
 * @constructor
 * Create an instance of the tutorial's game logic
 *
 * @param assetManager AssetManager instance to get the mp3 files
 */
class GameTutorial(
    gameInstance: GameInstance,
    assetManager: AssetManager,
    context: Context,
    private val resources: Resources
) : Game(gameInstance, assetManager, context) {

    override fun init() {


        /**
        this.gameSound = GameSound(
            Playlist(
            ids.map {ResourceMusicReference(context, it, resources)}
            )
        )
        **/

        this.gameSound = GameSound(
            game.playlist,
            context, resources
        )
    }
}