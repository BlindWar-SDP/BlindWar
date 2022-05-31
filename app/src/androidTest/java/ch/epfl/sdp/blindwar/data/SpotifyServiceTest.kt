package ch.epfl.sdp.blindwar.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.data.spotify.SpotifyApiConstants.GRANT_TYPE
import ch.epfl.sdp.blindwar.data.spotify.SpotifyApiConstants.credentialsEncoding
import ch.epfl.sdp.blindwar.data.spotify.SpotifyApiConstants.tokenParameter
import ch.epfl.sdp.blindwar.data.spotify.SpotifyService.spotifyApiFactory
import ch.epfl.sdp.blindwar.data.spotify.SpotifyToken
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * TODO: Add the following tests :
 * ** correctSearchTrackTest()
 */
@RunWith(AndroidJUnit4::class)
class SpotifyServiceTest {
    private lateinit var artistId: String
    private lateinit var spotifyResponse: String
    private lateinit var spotifyToken: String
    private lateinit var artistPath: String
    private lateinit var tokenPath: String
    private lateinit var testPath: String
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        artistPath = "artists/"
        tokenPath = "api/token/"
        testPath = "test/"
        spotifyResponse = "{\n" +
                "  \"external_urls\": {\n" +
                "    \"spotify\": \"https://open.spotify.com/artist/3fMbdgg4jU18AjLCKBhRSm\"\n" +
                "  },\n" +
                "  \"followers\": {\n" +
                "    \"href\": null,\n" +
                "    \"total\": 22104720\n" +
                "  },\n" +
                "  \"genres\": [\n" +
                "    \"pop\",\n" +
                "    \"r&b\",\n" +
                "    \"soul\"\n" +
                "  ],\n" +
                "  \"href\": \"https://api.spotify.com/v1/artists/3fMbdgg4jU18AjLCKBhRSm\",\n" +
                "  \"id\": \"3fMbdgg4jU18AjLCKBhRSm\",\n" +
                "  \"images\": [\n" +
                "    {\n" +
                "      \"height\": 640,\n" +
                "      \"url\": \"https://i.scdn.co/image/ab6761610000e5eba2a0b9e3448c1e702de9dc90\",\n" +
                "      \"width\": 640\n" +
                "    },\n" +
                "    {\n" +
                "      \"height\": 320,\n" +
                "      \"url\": \"https://i.scdn.co/image/ab67616100005174a2a0b9e3448c1e702de9dc90\",\n" +
                "      \"width\": 320\n" +
                "    },\n" +
                "    {\n" +
                "      \"height\": 160,\n" +
                "      \"url\": \"https://i.scdn.co/image/ab6761610000f178a2a0b9e3448c1e702de9dc90\",\n" +
                "      \"width\": 160\n" +
                "    }\n" +
                "  ],\n" +
                "  \"name\": \"Michael Jackson\",\n" +
                "  \"popularity\": 85,\n" +
                "  \"type\": \"artist\",\n" +
                "  \"uri\": \"spotify:artist:3fMbdgg4jU18AjLCKBhRSm\"\n" +
                "}"

        spotifyToken = "{\n" +
                "   \"access_token\": \"NgCXRKcMzYjw\",\n" +
                "   \"token_type\": \"bearer\",\n" +
                "   \"expires_in\": 3600\n" +
                "}"

        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @Test
    fun correctRequestGetArtist() {
        val mockSpotifyApi = spotifyApiFactory(mockWebServer.url(testPath).toString()).value
        //val metaSpotifyApi = apiMeta.value

        val mockResponse = MockResponse()
            .addHeader("Content-Type", "application/json")
            .setBody(spotifyResponse)
        mockWebServer.enqueue(mockResponse)
        artistId = "3fMbdgg4jU18AjLCKBhRSm"

        val token = SpotifyToken("NgCXRKcMzYjw", 3600, "bearer")

        runBlocking { mockSpotifyApi.getArtist(tokenParameter(token), artistId) }
        val response = mockWebServer.takeRequest()

        assertEquals("GET", response.method.toString())
        assertEquals("application/json", response.getHeader("Content-Type").toString())
        assertNotNull(response.getHeader("Authorization"))
        assertEquals("/${testPath}${artistPath}${artistId}", response.path.toString())

        mockWebServer.shutdown()
    }

    @Test
    fun correctRequestGetToken() {
        val mockSpotifyApi = spotifyApiFactory(mockWebServer.url(testPath).toString()).value

        val mockResponse = MockResponse()
            .addHeader("Content-Type", "application/json")
            .setBody(spotifyToken)

        mockWebServer.enqueue(mockResponse)

        runBlocking { mockSpotifyApi.getToken(credentialsEncoding(), GRANT_TYPE) }
        val response = mockWebServer.takeRequest()

        assertEquals("POST", response.method.toString())
        assertEquals(
            "application/x-www-form-urlencoded",
            response.getHeader("Content-Type").toString()
        )
        assertNotNull(response.getHeader("Authorization"))
        assertEquals("/${testPath}${tokenPath}", response.path.toString())
    }

    @Test
    fun correctSearchTest() {
        val mockSpotifyApi = spotifyApiFactory(mockWebServer.url(testPath).toString()).value
        //val tokenSpotifyApi = apiAuth.value

        val mockResponse = MockResponse()
            .addHeader("Content-Type", "application/json")
            .setBody(spotifyToken)

        mockWebServer.enqueue(mockResponse)

        runBlocking { mockSpotifyApi.getToken(credentialsEncoding(), GRANT_TYPE) }
        val response = mockWebServer.takeRequest()

        assertEquals("POST", response.method.toString())
        assertEquals(true, false)
        assertEquals(
            "application/x-www-form-urlencoded",
            response.getHeader("Content-Type").toString()
        )
        assertNotNull(response.getHeader("Authorization"))
        assertEquals("/${testPath}${tokenPath}", response.path.toString())
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}