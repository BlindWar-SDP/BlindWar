package ch.epfl.sdp.blindwar.data.music.metadata
import junit.framework.TestCase
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat


class URIMusicMetadataTest: TestCase() {

    fun testTestToString() {
        val expected = "Poker Face by Lady Gaga"
        val uriMetadata = URIMusicMetadata("Poker Face", "Lady Gaga", "", 0, "URI")
        assertThat(uriMetadata.toString(), CoreMatchers.`is`(expected))
    }

    fun testGetTitle() {
        val expected = "Lady Gaga"
        val uriMetadata = URIMusicMetadata("Poker Face", "", "", 0, "URI")
        uriMetadata.artist = expected
        assertThat(uriMetadata.artist, CoreMatchers.`is`(expected))
    }

    fun testGetArtist() {
        val expected = "Shine On You Crazy Diamond"
        val uriMetadata = URIMusicMetadata("", "Pink Floyd", "", 650000, "URI")
        uriMetadata.title = expected
        assertThat(uriMetadata.title, CoreMatchers.`is`(expected))
    }

    fun testGetImageUrl() {
        val expected = "https://i.scdn.co/image/ab67616d0000b273b33d46dfa2635a47eebf63b2"
        val uriMetadata = URIMusicMetadata("Shine On You Crazy Diamond", "Pink Floyd", "", 650000, "URI")
        uriMetadata.imageUrl = expected
        assertThat(uriMetadata.imageUrl, CoreMatchers.`is`(expected))
    }

    fun testGetDuration() {
        val expected = 90
        val uriMetadata = URIMusicMetadata("Shine On You Crazy Diamond", "Pink Floyd", "", 650000)
        uriMetadata.duration = expected
        assertThat(uriMetadata.duration, CoreMatchers.`is`(expected))
    }

    fun testGetURI() {
        val expected = 90
        val uriMetadata = URIMusicMetadata("Shine On You Crazy Diamond", "Pink Floyd", "", 650000, "URI")
        uriMetadata.duration = expected
        assertThat(uriMetadata.uri, CoreMatchers.`is`("URI"))
    }
}