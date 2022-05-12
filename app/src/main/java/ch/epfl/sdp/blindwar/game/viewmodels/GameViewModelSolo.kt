package ch.epfl.sdp.blindwar.game.viewmodels

import android.content.Context
import android.content.res.Resources
import android.os.Build
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata
import ch.epfl.sdp.blindwar.game.model.GameResult
import ch.epfl.sdp.blindwar.game.model.config.GameInstance
import ch.epfl.sdp.blindwar.game.model.config.GameMode
import ch.epfl.sdp.blindwar.game.util.GameHelper
import ch.epfl.sdp.blindwar.profile.model.Mode
import ch.epfl.sdp.blindwar.profile.model.Result
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Class representing an instance of a game
 *
 * @param gameInstance object that defines the parameters / configuration of a game
 * @param context of the Game
 * @constructor Construct a class that represent the game logic
 */
class GameViewModelSolo(
    gameInstance: GameInstance,
    context: Context,
    resources: Resources
) : GameViewModel(gameInstance, context, resources) {

    /**
     * Record the game instance to the player history
     * clean up player and assets
     *
     */
    override fun endGame() {
        val fails = round - score
        val result: Result = if (fails == 0) {
            Result.WIN
        } else {
            Result.LOSS
        }
        var formatted = "never"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            formatted = current.format(formatter)
        }
        val gameResult = GameResult(mode, Mode.SOLO, result, round, score, formatted)

        profileViewModel.updateStats(score, fails, gameResult)
        musicViewModel.soundTeardown()
    }

    /**
     * Pass to the next round
     *
     * @return true if the game is over after this round, false otherwise
     */
    override fun nextRound(): Boolean {
        if (mode == GameMode.SURVIVAL && lives.value!! <= 0) {
            endGame()
            return true
        }

        if (round >= gameParameter.round) {
            endGame()
            return true
        }

        musicViewModel.nextRound()
        musicViewModel.normalMode()
        return false
    }

    /**
     * Depends on the game instance parameter
     *
     * @return
     */
    override fun currentMetadata(): MusicMetadata? {
        if (gameParameter.hint) {
            return musicViewModel.getCurrentMetadata()
        }

        return null
    }

    /**
     * Try to guess a music by its title
     *
     * @param titleGuess Title that the user guesses
     * @return True if the guess is correct
     */
    override fun guess(titleGuess: String, isVocal: Boolean): Boolean {
        return if (
            GameHelper.isTheCorrectTitle(titleGuess, currentMetadata()!!.title, isVocal)
        ) {
            score += 1
            round += 1
            musicViewModel.summaryMode()
            true
        } else
            false
    }

    /**
     * Current round has timed out
     *
     */
    override fun timeout() {
        round += 1
        lives.value = lives.value?.minus(1)
    }
}