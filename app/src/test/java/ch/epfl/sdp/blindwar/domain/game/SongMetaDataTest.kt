package ch.epfl.sdp.blindwar.domain.game

import ch.epfl.sdp.blindwar.ui.SongMetaData
import junit.framework.TestCase
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat

class SongMetaDataTest : TestCase() {

    fun testTestToString() {
        val expected = "Shine On You Crazy Diamond by Pink Floyd"
        val musicMetaData = SongMetaData("Shine On You Crazy Diamond", "Pink Floyd")
        assertThat(musicMetaData.toString(), `is`(expected))
    }

    fun testGetTitle() {
        val expected = "Pink Floyd"
        val musicMetaData = SongMetaData("Shine On You Crazy Diamond", "Pink Floyd")
        assertThat(musicMetaData.artist, `is`(expected))
    }

    fun testGetArtist() {
        val expected = "Shine On You Crazy Diamond"
        val musicMetaData = SongMetaData("Shine On You Crazy Diamond", "Pink Floyd")
        assertThat(musicMetaData.title, `is`(expected))
    }
}