package ch.epfl.sdp.blindwar

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.blindwar.game.GameTutorial
import ch.epfl.sdp.blindwar.game.MusicMetaData
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GreetingActivityTest {
    @Test
    fun checkMessage() {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            GreetingActivity::class.java
        ).apply {
            putExtra(EXTRA_MESSAGE, "BlindWar")
        }
        ActivityScenario.launch<GreetingActivity>(intent)
            .use { scenario -> onView(withId(R.id.textViewGreeting)).check(matches(withText("BlindWar"))) }
    }


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
}