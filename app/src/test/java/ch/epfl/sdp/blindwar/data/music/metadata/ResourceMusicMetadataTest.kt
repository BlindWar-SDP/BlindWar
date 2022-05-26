package ch.epfl.sdp.blindwar.data.music.metadata

import junit.framework.TestCase
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert


class ResourceMusicMetadataTest : TestCase() {
    fun testTestToString() {
        val expected = "Poker Face by Lady Gaga"
        val uriMetadata = ResourceMusicMetadata("Poker Face", "Lady Gaga", "", 0)
        MatcherAssert.assertThat(uriMetadata.toString(), CoreMatchers.`is`(expected))
    }

    fun testGetTitle() {
        val expected = "Lady Gaga"
        val uriMetadata = ResourceMusicMetadata("Poker Face", "", "", 0)
        uriMetadata.artist = expected
        MatcherAssert.assertThat(uriMetadata.artist, CoreMatchers.`is`(expected))
    }

    fun testGetArtist() {
        val expected = "Shine On You Crazy Diamond"
        val uriMetadata = ResourceMusicMetadata("", "Pink Floyd", "", 650000)
        uriMetadata.title = expected
        MatcherAssert.assertThat(uriMetadata.title, CoreMatchers.`is`(expected))
    }

    fun testGetImageUrl() {
        val expected = "https://i.scdn.co/image/ab67616d0000b273b33d46dfa2635a47eebf63b2"
        val uriMetadata =
            ResourceMusicMetadata("Shine On You Crazy Diamond", "Pink Floyd", "", 650000)
        uriMetadata.imageUrl = expected
        MatcherAssert.assertThat(uriMetadata.imageUrl, CoreMatchers.`is`(expected))
    }

    fun testGetDuration() {
        val expected = 90
        val uriMetadata =
            ResourceMusicMetadata("Shine On You Crazy Diamond", "Pink Floyd", "", 650000)
        uriMetadata.duration = expected
        MatcherAssert.assertThat(uriMetadata.duration, CoreMatchers.`is`(expected))
    }
}