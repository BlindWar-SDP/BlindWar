package ch.epfl.sdp.blindwar.data.music.metadata

import ch.epfl.sdp.blindwar.game.util.GameUtil.metadataTutorial
import junit.framework.TestCase
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat

class MusicMetadataTest : TestCase() {

    fun testTestToString() {
        val expected = "Poker Face by Lady Gaga"
        val musicMetaData =
            MusicMetadata(name = "Poker Face", author = "Lady Gaga", previewUrl = "", duration = 0)
        assertThat(musicMetaData.toString(), `is`(expected))
    }

    fun testGetArtist() {
        val expected = "Lady Gaga"
        val musicMetaData = metadataTutorial()["Lady Gaga"]!!
        assertThat(musicMetaData.author, `is`(expected))
    }

    fun testGetArtistWithMethod() {
        val expected = "Lady Gaga"
        val musicMetaData = metadataTutorial()["Lady Gaga"]!!
        assertThat(musicMetaData.author, `is`(expected))
    }

    fun testGetTitle() {
        val expected = "Shine On You Crazy Diamond"
        val musicMetaData =
            MusicMetadata(
                name = "Shine On You Crazy Diamond",
                author = "Pink Floyd",
                previewUrl = "",
                duration = 650000
            )
        assertThat(musicMetaData.name, `is`(expected))
    }

    fun testGetTitleWithMethod() {
        val expected = "Shine On You Crazy Diamond"
        val musicMetaData =
            MusicMetadata(
                name = "Shine On You Crazy Diamond",
                author = "Pink Floyd",
                previewUrl = "",
                duration = 650000
            )
        assertThat(musicMetaData.name, `is`(expected))
    }

    fun testGetImageUrl() {
        val expected = "https://i.scdn.co/image/ab67616d0000b273b33d46dfa2635a47eebf63b2"
        val musicMetaData = metadataTutorial()["Daft Punk"]!!
        assertThat(musicMetaData.cover, `is`(expected))
    }

    fun testGetImageUrlWithMethod() {
        val expected = "https://i.scdn.co/image/ab67616d0000b273b33d46dfa2635a47eebf63b2"
        val musicMetaData = metadataTutorial()["Daft Punk"]!!
        assertThat(musicMetaData.cover, `is`(expected))
    }

    fun testGetLevel() {
        val expected = ""
        val musicMetaData = metadataTutorial()["Daft Punk"]!!
        assertThat(musicMetaData.level, `is`(expected))
    }

    fun testGetGenre() {
        val expected = ""
        val musicMetaData = metadataTutorial()["Daft Punk"]!!
        assertThat(musicMetaData.genre, `is`(expected))
    }

    fun testGetPreviewUrl() {
        val expected = ""
        val musicMetaData = metadataTutorial()["Daft Punk"]!!
        assertThat(musicMetaData.previewUrl, `is`(expected))
    }

    fun testGetSize() {
        val expected = 0
        val musicMetaData = metadataTutorial()["Daft Punk"]!!
        assertThat(musicMetaData.size, `is`(expected))
    }

    fun testExtendable() {
        val expected = false
        val musicMetaData = metadataTutorial()["Daft Punk"]!!
        assertThat(musicMetaData.extendable, `is`(expected))
    }
}