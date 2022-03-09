package ch.epfl.sdp.blindwar.game

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

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


    /**
     * Test if we can start all music at only once and then return null
     *
     */
    @Test
    fun testNextRound() {
        val gameTutorial = GameTutorial(InstrumentationRegistry.getInstrumentation().targetContext.assets)

        assertTrue(true)
        // Iterate 10 times since we have 10 different musics in tutorial
        var toPlay = expectedMusics.copyOf().toMutableSet()
        for(i in 0..9){
            // Get the current music information
            val currentMetaData = gameTutorial?.nextRound()

            assertTrue("$currentMetaData not found in the list of music to play", toPlay.remove(currentMetaData))
        }
    }
}