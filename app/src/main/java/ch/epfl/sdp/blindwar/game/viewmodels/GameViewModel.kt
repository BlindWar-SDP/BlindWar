package ch.epfl.sdp.blindwar.game.viewmodels

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.blindwar.audio.MusicViewModel
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata
import ch.epfl.sdp.blindwar.game.model.config.GameInstance
import ch.epfl.sdp.blindwar.game.model.config.GameMode
import ch.epfl.sdp.blindwar.game.model.config.GameParameter
import ch.epfl.sdp.blindwar.profile.viewmodel.ProfileViewModel

/**
 * Class representing an instance of a game
 *
 * @param gameInstance object that defines the parameters / configuration of a game
 * @param context of the Game
 * @constructor Construct a class that represent the game logic
 */
abstract class GameViewModel(
    gameInstance: GameInstance,
    protected val context: Context,
    private val resources: Resources
) : ViewModel() {
    /** Encapsulates the characteristics of a game instead of its logic
     *
     */
    protected val game: GameInstance = gameInstance
    protected lateinit var musicViewModel: MusicViewModel
    protected val profileViewModel = ProfileViewModel()

    protected val gameParameter: GameParameter = gameInstance
        .gameConfig
        .parameter

    protected val mode: GameMode = gameInstance
        .gameConfig
        .mode

    /** Player game score **/
    var score = 0
        protected set

    var round = 0
        protected set

    /** Survival mode specific **/
    val lives = MutableLiveData(gameParameter.lives)

    /**
     * Prepares the game following the configuration
     *
     */
    fun init() {
        musicViewModel = MusicViewModel(
            game.onlinePlaylist,
            context, resources
        )
    }


    /**
     * Record the game instance to the player history
     * clean up player and assets
     *
     */
    protected abstract fun endGame()

    /**
     * Pass to the next round
     *
     * @return true if the game is over after this round, false otherwise
     */
    abstract fun nextRound(): Boolean

    /**
     * Depends on the game instance parameter
     *
     * @return
     */
    abstract fun currentMetadata(): MusicMetadata?

    /**
     * Try to guess a music by its title
     *
     * @param titleGuess Title that the user guesses
     * @return True if the guess is correct
     */
    abstract fun guess(titleGuess: String, isVocal: Boolean): Boolean

    /**
     * Current round has timed out
     *
     */
    abstract fun timeout()

    /**
     * Play the current music if in pause
     *
     */
    fun play() {
        musicViewModel.play()
    }

    /**
     * Pause the current music if playing
     *
     */
    fun pause() {
        musicViewModel.pause()
    }
}