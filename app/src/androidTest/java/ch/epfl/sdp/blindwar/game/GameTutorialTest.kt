package ch.epfl.sdp.blindwar.game

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.empty

@RunWith(AndroidJUnit4::class)
class GameTutorialTest {
    // All possible musics during tutorial
    private val expectedMusics = arrayOf(
        MusicMetaData("Highway to Hell", "ACDC"),
        MusicMetaData("Harder Better Faster Stronger", "Daft Punk"),
        MusicMetaData("One More Time", "Daft Punk"),
        MusicMetaData("Feel Good Inc", "Gorillaz"),
        MusicMetaData("Poker Face", "Lady Gaga"),
        MusicMetaData("Californication", "Red Hot Chili Peppers"),
        MusicMetaData("Mistral gagnant", "Renaud"),
        MusicMetaData("In Too Deep", "Sum 41"),
        MusicMetaData("London Calling", "The Clash"),
        MusicMetaData("Respect", "The Notorious BIG"),
    )

    @Test
    fun testNextRound() {
        //val gameTutorial = GameTutorial(InstrumentationRegistry.getInstrumentation().targetContext.assets)
        val gameTutorial = GameTutorial(ApplicationProvider.getApplicationContext<Context>().assets)

        // Iterate 10 times since we have 10 different musics in tutorial
        val toPlay = expectedMusics.copyOf().toMutableSet()
        for(i in 0..9){
            // Get the current music information
            val currentMetaData = gameTutorial.nextRound()

            assertThat(toPlay.remove(currentMetaData), `is`(true))
        }

        assertThat(toPlay, empty())
    }

    @Test
    fun testTwoGoodGuesses() {
        val gameTutorial = GameTutorial(ApplicationProvider.getApplicationContext<Context>().assets)
        val music1 = gameTutorial.nextRound()
        music1?.let { gameTutorial.guess(it.title) }

        val music2 = gameTutorial.nextRound()
        music2?.let { gameTutorial.guess(it.title) }

        assertThat(gameTutorial.score, `is`(2))
    }

    @Test
    fun testTwoGoodAndOneBadGuesses() {
        val gameTutorial = GameTutorial(ApplicationProvider.getApplicationContext<Context>().assets)
        val music1 = gameTutorial.nextRound()
        music1?.let { gameTutorial.guess(it.title) }

        val music2 = gameTutorial.nextRound()
        gameTutorial.guess("THIS IS NOT A CORRECT TITLE")
        music2?.let { gameTutorial.guess(it.title) }

        assertThat(gameTutorial.score, `is`(2))
    }
}