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
class GameSolo(private val gameInstance: GameInstance,
                   private val assetManager: AssetManager) : Game(gameInstance) {

    override val gameSound = GameSound(assetManager)

    override fun init() {
        gameSound.soundInit(gameInstance.playlist)
    }

    override fun endGame() {
        gameSound.soundTeardown()
    }

    /**
     * Depends on the game instance parameter
     */
    fun currentMetadata(): SongMetaData? {
        if (gameParameter.hint) {
            return gameSound.getCurrentMetadata()
        }

        return null
    }

    /**
     * Logic to pass to the next round of the game
     */
    override fun nextRound(): Boolean {
        if (round >= gameParameter.round) {
            endGame()
            return true
        }

        gameSound.nextRound()

        return false
    }

    override fun guess(titleGuess: String): Boolean {
        return if (titleGuess.uppercase(Locale.getDefault()) == currentMetadata()?.title?.uppercase(Locale.getDefault())) {
            score += 1
            true
        } else
            false
    }

    override fun play() {
        gameSound.play()
    }

    override fun pause() {
        gameSound.pause()
    }
}