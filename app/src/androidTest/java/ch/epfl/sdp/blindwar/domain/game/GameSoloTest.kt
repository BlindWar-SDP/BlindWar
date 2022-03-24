package ch.epfl.sdp.blindwar.domain.game

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.domain.game.SongImageUrlConstants.SONG_MAP
import junit.framework.Assert.assertEquals
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.empty
import java.util.*

@RunWith(AndroidJUnit4::class)
class GameSoloTest {
    // All possible musics during tutorial

    private val assets = ApplicationProvider.getApplicationContext<Context>().assets

    @Test
    fun testNextRound() {
        val gameTutorial = GameSolo(Tutorial.gameInstance, assets)
        gameTutorial.init()
        val round = Tutorial
            .gameInstance
            .gameConfig
            .parameter
            .round

        // Iterate 10 times since we have 10 different musics in tutorial
        val toPlay: MutableSet<SongMetaData> = Tutorial.gameInstance.playlist.toMutableSet()
        for (i in 0 until round) {
            gameTutorial.nextRound()
            Log.d("BLAISE MATUIDI", gameTutorial.currentMetadata().toString())
            assertThat(toPlay.remove(gameTutorial.currentMetadata()), `is`(true))
        }
    }


    @Test
    fun testTwoGoodGuesses() {
        val gameTutorial = GameSolo(Tutorial.gameInstance, assets)
        gameTutorial.init()
        goodGuess(gameTutorial)
        goodGuess(gameTutorial)

        assertThat(gameTutorial.score, `is`(2))
    }

    @Test
    fun testUpperCaseGuess() {
        val gameTutorial = GameSolo(Tutorial.gameInstance, assets)
        gameTutorial.init()
        gameTutorial.nextRound()
        val music1 = gameTutorial.currentMetadata()
        music1?.let { gameTutorial.guess(it.title.uppercase(Locale.getDefault())) }

        assertThat(gameTutorial.score, `is`(1))
    }

    @Test
    fun testTwoGoodAndOneBadGuesses() {
        val gameTutorial = GameSolo(Tutorial.gameInstance, assets)
        gameTutorial.init()
        goodGuess(gameTutorial)
        goodGuess(gameTutorial)
        badGuess(gameTutorial)

        assertThat(gameTutorial.score, `is`(2))
    }

    private fun goodGuess(gameTutorial: GameSolo) {
        gameTutorial.nextRound()
        gameTutorial.guess(gameTutorial.currentMetadata()?.title!!)
    }

    private fun badGuess(gameTutorial: GameSolo) {
        gameTutorial.nextRound()
        gameTutorial.guess("THIS IS NOT A CORRECT TITLE")
    }
}