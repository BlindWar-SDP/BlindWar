package ch.epfl.sdp.blindwar.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(AndroidJUnit4::class)
class SpotifyResponseTest {
    private lateinit var spotifyArtist: SpotifyArtist
    private lateinit var spotifyImage: SpotifyImage
    private lateinit var spotifyFollowers: SpotifyFollowers
    private lateinit var spotifyUrls: SpotifyExternalUrls
    private lateinit var spotifyToken: SpotifyToken

    @Before
    fun setUp() {

        spotifyFollowers = SpotifyFollowers("FollowersUrl", 9)
        spotifyImage = SpotifyImage(0, "", 0)
        spotifyUrls = SpotifyExternalUrls("SpotifyURL")
        spotifyToken = SpotifyToken("TOKEN", 0, "CREDENTIALS")
        spotifyArtist = SpotifyArtist(spotifyUrls,
                                      spotifyFollowers,
            arrayListOf("POP"),
            "ARTIST_URL",
        "ARTIST_ID",
        arrayListOf(spotifyImage),
        "ARTIST_NAME",
        10,
        "ARTIST_TYPE",
        "ARTIST_URI")
    }

    @Test
    fun testSpotifyArtist() {
    }

    @Test
    fun testSpotifyImage() {

    }

    @Test
    fun testSpotifyFollowers() {

    }

    @Test
    fun testSpotifyExternalUrls() {

    }

    @Test
    fun testSpotifyToken() {

    }
}