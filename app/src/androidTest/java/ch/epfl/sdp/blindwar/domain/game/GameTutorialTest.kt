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
class GameTutorialTest {
    // All possible musics during tutorial
    private val TIME_TO_FIND = 5000
    private val expectedMusics = arrayOf(
        //SongMetaData("Highway to Hell", "ACDC", SONG_MAP["ACDC"]!!),
        //SongMetaData("Harder Better Faster Stronger", "Daft Punk", SONG_MAP["Daft Punk"]!!),
        SongMetaData("One More Time", "Daft Punk", SONG_MAP["Daft Punk"]!!),
        SongMetaData("Feel Good Inc", "Gorillaz", SONG_MAP["Gorillaz"]!!),
        //SongMetaData("Poker Face", "Lady Gaga", SONG_MAP["Lady Gaga"]!!),
        //SongMetaData("Californication", "Red Hot Chili Peppers", SONG_MAP["Red Hot Chili Peppers"]!!),
        //SongMetaData("Mistral gagnant", "Renaud", SONG_MAP["Renaud"]!!),
        //SongMetaData("In Too Deep", "Sum 41", SONG_MAP["Sum 41"]!!),
        //SongMetaData("London Calling", "The Clash", SONG_MAP["The Clash"]!!),
        //SongMetaData("Respect", "The Notorious BIG", SONG_MAP["The Notorious BIG"]!!),
    )

    @Test
    fun testNextRound() {
        val gameTutorial = GameTutorial(ApplicationProvider.getApplicationContext<Context>().assets, TIME_TO_FIND)
        // Iterate 10 times since we have 10 different musics in tutorial
        val toPlay = expectedMusics.copyOf().toMutableSet()
        for (i in 0 until toPlay.size) {
            assertThat(toPlay.remove(gameTutorial.nextRound()), `is`(true))
        }

        // All songs should be removed
        assertEquals(toPlay.size, 0)
    }

    @Test
    fun testTwoGoodGuesses() {
        val gameTutorial = GameTutorial(ApplicationProvider.getApplicationContext<Context>().assets, TIME_TO_FIND)
        val music1 = gameTutorial.nextRound()
        music1?.let { gameTutorial.guess(it.title) }

        val music2 = gameTutorial.nextRound()
        music2?.let { gameTutorial.guess(it.title) }

        assertThat(gameTutorial.score, `is`(2))
    }

    @Test
    fun testUpperCaseGuess() {
        val gameTutorial = GameTutorial(ApplicationProvider.getApplicationContext<Context>().assets, 5000)
        val music1 = gameTutorial.nextRound()
        music1?.let { gameTutorial.guess(it.title.uppercase(Locale.getDefault())) }

        assertThat(gameTutorial.score, `is`(1))
    }

    @Test
    fun testTwoGoodAndOneBadGuesses() {
        val gameTutorial = GameTutorial(ApplicationProvider.getApplicationContext<Context>().assets, TIME_TO_FIND)
        val music1 = gameTutorial.nextRound()
        music1?.let { gameTutorial.guess(it.title) }

        val music2 = gameTutorial.nextRound()
        gameTutorial.guess("THIS IS NOT A CORRECT TITLE")
        music2?.let { gameTutorial.guess(it.title) }

        assertThat(gameTutorial.score, `is`(2))
    }
}