package ch.epfl.sdp.blindwar.data

import ch.epfl.sdp.blindwar.domain.game.SongImageUrlConstants.SONG_MAP
import ch.epfl.sdp.blindwar.domain.game.SongMetaData
import junit.framework.TestCase
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat

class SongMetaDataTest : TestCase() {

    fun testTestToString() {
        val expected = "Shine On You Crazy Diamond by Pink Floyd"
        val musicMetaData = SongMetaData("Shine On You Crazy Diamond", "Pink Floyd", "")
        assertThat(musicMetaData.toString(), `is`(expected))
    }

    fun testGetTitle() {
        val expected = "Pink Floyd"
        val musicMetaData = SongMetaData("Shine On You Crazy Diamond", "Pink Floyd", "")
        assertThat(musicMetaData.artist, `is`(expected))
    }

    fun testGetArtist() {
        val expected = "Shine On You Crazy Diamond"
        val musicMetaData = SongMetaData("Shine On You Crazy Diamond", "Pink Floyd", "")
        assertThat(musicMetaData.title, `is`(expected))
    }

    fun testGetImageUrl() {
        val expected = "https://i.scdn.co/image/ab67616d00001e02b33d46dfa2635a47eebf63b2"
        val musicMetaData = SongMetaData("One More Time", "Daft Punk", SONG_MAP["Daft Punk"]!!)
        assertThat(musicMetaData.imageUrl, `is`(expected))
    }
}