package ch.epfl.sdp.blindwar.data.spotify

import junit.framework.TestCase
import org.junit.Test

class SpotifyResponseTest: TestCase() {
    private var spotifyImage = SpotifyImage(0, "", 0)
    private var spotifyFollowers = SpotifyFollowers("FollowersUrl", 9)
    private var spotifyUrls = SpotifyExternalUrls("SpotifyURL")
    private var spotifyToken = SpotifyToken("TOKEN", 0, "CREDENTIALS")
    private var spotifyArtist = SpotifyArtist(
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
    private var spotifyExternalIds = ExternalIds("isrc")
    private var spotifyExternalUrls = ExternalUrls("spotify")
    private var spotifyExternalUrlsX = ExternalUrlsX("spotify")
    private var spotifyExternalUrlsXX = ExternalUrlsXX("spotify")
    private var spotifyExternalUrlsXXX = ExternalUrlsXXX("spotify")
    private var dummyArtist = Artist(
        spotifyExternalUrls,
        "href",
        "id",
        "name",
        "type",
        "uri"
    )

    private var dummyArtistX = ArtistX(
        spotifyExternalUrlsXX,
        "href",
        "id",
        "name",
        "type",
        "uri"
    )
    private var dummyAlbum = Album(
        "album_type",
        listOf(dummyArtist),
        spotifyExternalUrlsX,
        "href",
        "id",
        listOf(spotifyImage),
        "name",
        "release_date",
        "release_date_precision",
        1,
        "type",
        "uri"
    )
    private var dummySpotifyTrack = SpotifyTrack(
        dummyAlbum,
        listOf(dummyArtistX),
        1,
        2,
        true,
        spotifyExternalIds,
        spotifyExternalUrlsXXX,
        "href",
        "id",
        true,
        true,
        "name",
        3,
        "preview_url",
        4,
        "type",
        "uri"
    )
    private var dummySpotifySearchTrack = SpotifySearchTrack(listOf(dummySpotifyTrack))
    private var dummySpotifySearchTrackResult =  SpotifySearchTrackResult(
        "href",
        dummySpotifySearchTrack,
        1,
        "next",
        2,
        "previous",
        3
    )

    //End of setup, beginning of tests

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
        assertEquals(spotifyUrls, spotifyArtist.external_urls)
        assertEquals(spotifyFollowers, spotifyArtist.followers)
        assertEquals("ARTIST_ID", spotifyArtist.id)
        assertEquals(arrayListOf(spotifyImage), spotifyArtist.images)
        assertEquals("ARTIST_NAME", spotifyArtist.name)
        assertEquals(10, spotifyArtist.popularity)
        assertEquals(arrayListOf("POP"), spotifyArtist.genres)
        assertEquals("ARTIST_TYPE", spotifyArtist.type)
        assertEquals("ARTIST_URI", spotifyArtist.uri)
        assertEquals("ARTIST_URL", spotifyArtist.href)



        assertEquals(
            SpotifyArtist(
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
            ), spotifyArtist
        )
    }

    @Test
    fun testSpotifyExternalUrls() {
        assertEquals("SpotifyURL", spotifyUrls.spotify)
    }

    @Test
    fun testSpotifyToken() {
        spotifyToken.access_token = "TOKEN"
        assertEquals("TOKEN", spotifyToken.access_token)
        assertEquals(0, spotifyToken.expires_in)
        assertEquals("CREDENTIALS", spotifyToken.token_type)
        assertEquals(SpotifyToken("TOKEN", 0, "CREDENTIALS"), spotifyToken)
    }

    @Test
    fun testExternalIds() {
        assertEquals("isrc", spotifyExternalIds.isrc)
    }

    @Test
    fun testExternalUrlsX() {
        assertEquals("spotify", spotifyExternalUrlsX.spotify)
    }

    @Test
    fun testExternalUrlsXX() {
        assertEquals("spotify", spotifyExternalUrlsXX.spotify)
    }

    @Test
    fun testExternalUrlsXXX() {
        assertEquals("spotify", spotifyExternalUrlsXXX.spotify)
    }

    @Test
    fun testArtist() {
        assertEquals(spotifyExternalUrls, dummyArtist.external_urls)
        assertEquals("href", dummyArtist.href)
        assertEquals("id", dummyArtist.id)
        assertEquals("name", dummyArtist.name)
        assertEquals("type", dummyArtist.type)
        assertEquals("uri", dummyArtist.uri)
    }

    @Test
    fun testArtistX() {
        assertEquals(spotifyExternalUrlsXX, dummyArtistX.external_urls)
        assertEquals("href", dummyArtistX.href)
        assertEquals("id", dummyArtistX.id)
        assertEquals("name", dummyArtistX.name)
        assertEquals("type", dummyArtistX.type)
        assertEquals("uri", dummyArtistX.uri)
    }

    @Test
    fun testAlbum() {
        assertEquals("album_type", dummyAlbum.album_type)
        assert(dummyAlbum.artists.contains(dummyArtist))
        assertEquals(spotifyExternalUrlsX, dummyAlbum.external_urls)
        assertEquals("href", dummyAlbum.href)
        assertEquals("id", dummyAlbum.id)
        assert(dummyAlbum.images.contains(spotifyImage))
        assertEquals("name", dummyAlbum.name)
        assertEquals("release_date", dummyAlbum.release_date)
        assertEquals("release_date_precision", dummyAlbum.release_date_precision)
        assertEquals(1, dummyAlbum.total_tracks)
        assertEquals("type", dummyAlbum.type)
        assertEquals("uri", dummyAlbum.uri)

    }

    @Test
    fun testSpotifyTrack() {
        assertEquals(dummyAlbum, dummySpotifyTrack.album)
        assert(dummySpotifyTrack.artists.contains(dummyArtistX))
        assertEquals(1, dummySpotifyTrack.disc_number)
        assertEquals(2, dummySpotifyTrack.duration_ms)
        assertEquals(true, dummySpotifyTrack.explicit)
        assertEquals(spotifyExternalIds, dummySpotifyTrack.external_ids)
        assertEquals(spotifyExternalUrlsXXX, dummySpotifyTrack.external_urls)
        assertEquals("href", dummySpotifyTrack.href)
        assertEquals("id", dummySpotifyTrack.id)
        assertEquals(true, dummySpotifyTrack.is_local)
        assertEquals(true, dummySpotifyTrack.is_playable)
        assertEquals("name", dummySpotifyTrack.name)
        assertEquals(3, dummySpotifyTrack.popularity)
        assertEquals("preview_url", dummySpotifyTrack.preview_url)
        assertEquals(4, dummySpotifyTrack.track_number)
        assertEquals("type", dummySpotifyTrack.type)
        assertEquals("uri", dummySpotifyTrack.uri)

    }

    @Test
    fun testSpotifySearchTrack() {
        assert(dummySpotifySearchTrack.items.contains(dummySpotifyTrack))
    }

    @Test
    fun testSpotifySearchTrackResult() {
        assertEquals("href", dummySpotifySearchTrackResult.href)
        assertEquals(dummySpotifySearchTrack, dummySpotifySearchTrackResult.tracks)
        assertEquals(1, dummySpotifySearchTrackResult.limit)
        assertEquals("next", dummySpotifySearchTrackResult.next)
        assertEquals(2, dummySpotifySearchTrackResult.offset)
        assertEquals("previous", dummySpotifySearchTrackResult.previous)
        assertEquals(3, dummySpotifySearchTrackResult.total)
    }
}
