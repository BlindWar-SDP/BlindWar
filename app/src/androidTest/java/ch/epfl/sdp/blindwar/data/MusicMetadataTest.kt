package ch.epfl.sdp.blindwar.data

import ch.epfl.sdp.blindwar.data.music.MusicImageUrlConstants.METADATA_TUTORIAL_MUSICS_PER_AUTHOR
import ch.epfl.sdp.blindwar.data.music.MusicMetadata
import junit.framework.TestCase
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat

class MusicMetadataTest : TestCase() {

    fun testTestToString() {
        val expected = "Poker Face by Lady Gaga"
        val musicMetaData = MusicMetadata("Poker Face", "Lady Gaga", "", 0)
        assertThat(musicMetaData.toString(), `is`(expected))
    }

    fun testGetTitle() {
        val expected = "Lady Gaga"
        val musicMetaData = METADATA_TUTORIAL_MUSICS_PER_AUTHOR["Lady Gaga"]!!
        assertThat(musicMetaData.artist, `is`(expected))
    }

    fun testGetArtist() {
        val expected = "Shine On You Crazy Diamond"
        val musicMetaData = MusicMetadata("Shine On You Crazy Diamond", "Pink Floyd", "", 650000)
        assertThat(musicMetaData.title, `is`(expected))
    }

    fun testGetImageUrl() {
        val expected = "https://i.scdn.co/image/ab67616d00001e02b33d46dfa2635a47eebf63b2"
        val musicMetaData = METADATA_TUTORIAL_MUSICS_PER_AUTHOR["Daft Punk"]!!
        assertThat(musicMetaData.imageUrl, `is`(expected))
    }
}