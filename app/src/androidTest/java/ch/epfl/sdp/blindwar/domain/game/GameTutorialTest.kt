package ch.epfl.sdp.blindwar.domain.game

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.data.music.MusicMetadata
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class GameTutorialTest {
    // All possible musics during tutorial

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val assets = context.assets

    @Test
    fun testNextRound() {
        val gameTutorial = GameTutorial(Tutorial.gameInstance, assets, context, context.resources)
        gameTutorial.init()
        val round = Tutorial
            .gameInstance
            .gameConfig
            .parameter
            .round

        // Iterate 10 times since we have 10 different musics in tutorial
        val toPlay: MutableSet<MusicMetadata> = Tutorial.gameInstance.playlist.toMutableSet()
        for (i in 0 until round) {
            gameTutorial.nextRound()
            assertThat(toPlay.remove(gameTutorial.currentMetadata()), `is`(true))
        }
    }


    @Test
    fun testTwoGoodGuesses() {
        val gameTutorial = GameTutorial(Tutorial.gameInstance, assets, context, context.resources)
        gameTutorial.init()
        goodGuess(gameTutorial)
        goodGuess(gameTutorial)

        assertThat(gameTutorial.score, `is`(2))
    }

    @Test
    fun testUpperCaseGuess() {
        val gameTutorial = GameTutorial(Tutorial.gameInstance, assets, context, context.resources)
        gameTutorial.init()
        gameTutorial.nextRound()
        val music1 = gameTutorial.currentMetadata()
        music1?.let { gameTutorial.guess(it.title.uppercase(Locale.getDefault()), false) }

        assertThat(gameTutorial.score, `is`(1))
    }

    @Test
    fun testTwoGoodAndOneBadGuesses() {
        val gameTutorial = GameTutorial(Tutorial.gameInstance, assets, context, context.resources)
        gameTutorial.init()
        goodGuess(gameTutorial)
        goodGuess(gameTutorial)
        badGuess(gameTutorial)

        assertThat(gameTutorial.score, `is`(2))
    }

    private fun goodGuess(gameTutorial: GameTutorial) {
        gameTutorial.nextRound()
        gameTutorial.guess(gameTutorial.currentMetadata()?.title!!, false)
    }

    private fun badGuess(gameTutorial: GameTutorial) {
        gameTutorial.nextRound()
        gameTutorial.guess("THIS IS NOT A CORRECT TITLE", false)
    }
}