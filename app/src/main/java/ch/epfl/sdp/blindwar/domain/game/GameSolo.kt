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

    override val gameSoundController = GameSoundController(assetManager)

    override fun init() {
        gameSoundController.soundInit(gameInstance.playlist)
    }

    override fun endGame() {
        gameSoundController.soundTeardown()
    }

    /**
     * Depends on the game instance parameter
     */
    fun currentMetadata(): SongMetaData? {
        if (gameParameter.hint) {
            return gameSoundController.getCurrentMetadata()
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

        gameSoundController.nextRound()
        return false
    }

    override fun guess(titleGuess: String): Boolean {
        return if (titleGuess.uppercase(Locale.getDefault()) == currentMetadata()?.title?.uppercase(Locale.getDefault())) {
            score += 1
            round += 1
            true
        } else
            false
    }

    override fun timeout() {
        round += 1
    }

    override fun play() {
        gameSoundController.play()
    }

    override fun pause() {
        gameSoundController.pause()
    }
}