package ch.epfl.sdp.blindwar.domain.game

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.data.music.Playlist
import ch.epfl.sdp.blindwar.data.music.fetcher.ResourceFetcher


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
        val ids = listOf(
            R.raw.acdc_highway_to_hell,
            R.raw.daft_punk_one_more_time,
            R.raw.gorillaz_feel_good,
            R.raw.lady_gaga_poker_face,
            R.raw.red_hot_chili_peppers_californication,
            R.raw.renaud_mistral_gagnant,
            R.raw.sum_41_in_too_deep,
            R.raw.the_clash_london_calling,
            R.raw.the_notorious_big_respect
        )

        this.musicSound = MusicSound(
            Playlist(
                ids.map {ResourceFetcher(context, it, resources)}
            )
        )
    }
}