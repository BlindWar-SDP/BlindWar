package ch.epfl.sdp.blindwar.data.music.metadata

import ch.epfl.sdp.blindwar.game.util.GameUtil.metadataTutorial
import junit.framework.TestCase
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat

class MusicMetadataTest : TestCase() {

    fun testTestToString() {
        val expected = "Poker Face by Lady Gaga"
        val musicMetaData = URIMusicMetadata("Poker Face", "Lady Gaga", "", 0, "URI")
        assertThat(musicMetaData.toString(), `is`(expected))
    }

    fun testGetTitle() {
        val expected = "Lady Gaga"
        val musicMetaData = metadataTutorial()["Lady Gaga"]!!
        assertThat(musicMetaData.artist, `is`(expected))
    }

    fun testGetArtist() {
        val expected = "Shine On You Crazy Diamond"
        val musicMetaData =
            URIMusicMetadata("Shine On You Crazy Diamond", "Pink Floyd", "", 650000, "URI")
        assertThat(musicMetaData.title, `is`(expected))
    }

    fun testGetImageUrl() {
        val expected = "https://i.scdn.co/image/ab67616d0000b273b33d46dfa2635a47eebf63b2"
        val musicMetaData = metadataTutorial()["Daft Punk"]!!
        assertThat(musicMetaData.imageUrl, `is`(expected))
    }
}