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

    fun testGetArtist() {
        val expected = "Lady Gaga"
        val musicMetaData = metadataTutorial()["Lady Gaga"]!!
        assertThat(musicMetaData.artist, `is`(expected))
    }

    fun testGetArtistWithMethod() {
        val expected = "Lady Gaga"
        val musicMetaData = metadataTutorial()["Lady Gaga"]!!
        assertThat(musicMetaData.getAuthor(), `is`(expected))
    }

    fun testGetTitle() {
        val expected = "Shine On You Crazy Diamond"
        val musicMetaData =
            URIMusicMetadata("Shine On You Crazy Diamond", "Pink Floyd", "", 650000, "URI")
        assertThat(musicMetaData.title, `is`(expected))
    }

    fun testGetTitleWithMethod() {
        val expected = "Shine On You Crazy Diamond"
        val musicMetaData =
            URIMusicMetadata("Shine On You Crazy Diamond", "Pink Floyd", "", 650000, "URI")
        assertThat(musicMetaData.getName(), `is`(expected))
    }

    fun testGetImageUrl() {
        val expected = "https://i.scdn.co/image/ab67616d0000b273b33d46dfa2635a47eebf63b2"
        val musicMetaData = metadataTutorial()["Daft Punk"]!!
        assertThat(musicMetaData.imageUrl, `is`(expected))
    }

    fun testGetImageUrlWithMethod() {
        val expected = "https://i.scdn.co/image/ab67616d0000b273b33d46dfa2635a47eebf63b2"
        val musicMetaData = metadataTutorial()["Daft Punk"]!!
        assertThat(musicMetaData.getCover(), `is`(expected))
    }

    fun testGetLevel() {
        val expected = ""
        val musicMetaData = metadataTutorial()["Daft Punk"]!!
        assertThat(musicMetaData.getLevel(), `is`(expected))
    }

    fun testGetGenre() {
        val expected = ""
        val musicMetaData = metadataTutorial()["Daft Punk"]!!
        assertThat(musicMetaData.getGenre(), `is`(expected))
    }

    fun testGetPreviewUrl() {
        val expected = ""
        val musicMetaData = metadataTutorial()["Daft Punk"]!!
        assertThat(musicMetaData.getPreviewUrl(), `is`(expected))
    }

    fun testGetSize() {
        val expected = 0
        val musicMetaData = metadataTutorial()["Daft Punk"]!!
        assertThat(musicMetaData.getSize(), `is`(expected))
    }

    fun testExtendable() {
        val expected = false
        val musicMetaData = metadataTutorial()["Daft Punk"]!!
        assertThat(musicMetaData.extendable(), `is`(expected))
    }
}