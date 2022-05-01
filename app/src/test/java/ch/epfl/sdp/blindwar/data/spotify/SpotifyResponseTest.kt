package ch.epfl.sdp.blindwar.data.spotify

import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class SpotifyResponseTest: TestCase() {
    private lateinit var spotifyArtist: SpotifyArtist
    private lateinit var spotifyImage: SpotifyImage
    private lateinit var spotifyFollowers: SpotifyFollowers
    private lateinit var spotifyUrls: SpotifyExternalUrls
    private lateinit var spotifyToken: SpotifyToken

    /**
    @Before
    fun setUp() {
        spotifyFollowers = SpotifyFollowers("FollowersUrl", 9)
        spotifyImage = SpotifyImage(0, "", 0)
        spotifyUrls = SpotifyExternalUrls("SpotifyURL")
        spotifyToken = SpotifyToken("TOKEN", 0, "CREDENTIALS")
        spotifyArtist = SpotifyArtist(
            spotifyUrls,
            spotifyFollowers,
            arrayListOf("POP"),
            "ARTIST_URL",
            "ARTIST_ID",
            arrayListOf(spotifyImage),
            "ARTIST_NAME",
            10,
            "ARTIST_TYPE",
            "ARTIST_URI"
        )
    }

    @Test
    fun testSpotifyFollowers() {
        assertEquals("FollowersUrl", spotifyFollowers.href)
        assertEquals(9, spotifyFollowers.total)
    }

    @Test
    fun testSpotifyImage() {
        assertEquals(0, spotifyImage.height)
        assertEquals("", spotifyImage.url)
        assertEquals(0, spotifyImage.width)
    }

    @Test
    fun testSpotifyArtist() {
        assertEquals(spotifyArtist.external_urls, spotifyUrls)
        assertEquals(spotifyArtist.followers, spotifyFollowers)
        assertEquals(spotifyArtist.id, "ARTIST_ID")
        assertEquals(spotifyArtist.images, arrayListOf(spotifyImage))
        assertEquals(spotifyArtist.name, "ARTIST_NAME")
        assertEquals(spotifyArtist.popularity, 10)
        assertEquals(spotifyArtist.genres, arrayListOf("POP"))
        assertEquals(spotifyArtist.type, "ARTIST_TYPE")
        assertEquals(spotifyArtist.uri, "ARTIST_URI")
        assertEquals(spotifyArtist.href, "ARTIST_URL")



        assertEquals(
            spotifyArtist, SpotifyArtist(
                spotifyUrls,
                spotifyFollowers,
                arrayListOf("POP"),
                "ARTIST_URL",
                "ARTIST_ID",
                arrayListOf(spotifyImage),
                "ARTIST_NAME",
                10,
                "ARTIST_TYPE",
                "ARTIST_URI"
            )
        )
    }

    @Test
    fun testSpotifyExternalUrls() {
        assertEquals(spotifyUrls.spotify, "SpotifyURL")
    }

    @Test
    fun testSpotifyToken() {
        spotifyToken.access_token = "TOKEN"
        assertEquals(spotifyToken.access_token, "TOKEN")
        assertEquals(spotifyToken.expires_in, 0)
        assertEquals(spotifyToken.token_type, "CREDENTIALS")
        assertEquals(spotifyToken, SpotifyToken("TOKEN", 0, "CREDENTIALS"))
    }**/
}
