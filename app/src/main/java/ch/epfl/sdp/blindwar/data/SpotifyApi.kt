package ch.epfl.sdp.blindwar.data

import ch.epfl.sdp.blindwar.data.SpotifyApiConstants.ACCEPT
import ch.epfl.sdp.blindwar.data.SpotifyApiConstants.API_PATH
import ch.epfl.sdp.blindwar.data.SpotifyApiConstants.ARTIST_ID
import ch.epfl.sdp.blindwar.data.SpotifyApiConstants.ARTIST_PATH
import ch.epfl.sdp.blindwar.data.SpotifyApiConstants.AUTH
import ch.epfl.sdp.blindwar.data.SpotifyApiConstants.CONTENT_TYPE
import ch.epfl.sdp.blindwar.data.SpotifyApiConstants.GRANT_TYPE
import ch.epfl.sdp.blindwar.data.SpotifyApiConstants.URL_ENCODED_FORM

import retrofit2.Response
import retrofit2.http.*


interface SpotifyApi {
    @GET(ARTIST_PATH)
    @Headers(
        ACCEPT,
        CONTENT_TYPE,
    )
    suspend fun getArtist(
        @Header(AUTH) token: String,
        @Path(ARTIST_ID) artist_id: String
    ): Response<SpotifyArtist>

    @POST(API_PATH)
    @FormUrlEncoded
    @Headers(URL_ENCODED_FORM)
    suspend fun getToken(
        @Header(AUTH) credentials: String,
        @Field(GRANT_TYPE) grant_type: String
    ): Response<SpotifyToken>

    /**
    @GET(ARTIST_PATH)
    @Headers(
    ACCEPT,
    CONTENT_TYPE,
    )
    suspend fun searchArtist(@Header(AUTH) token: String,
    @Path(ARTIST_ID) artist_id: String,
    @Query(LIMIT) limit: Int): Response<SpotifyArtist>

    @GET(TRACK_PATH)
    @Headers(
    ACCEPT,
    CONTENT_TYPE
    )
    suspend fun getTrack(@Header(AUTH) token: String,
    @Path(TRACK_ID) track_id: String): Response<SpotifyTrack>

    @GET(TRACK_PATH)
    @Headers(
    ACCEPT,
    CONTENT_TYPE
    )
    suspend fun searchTrack(@Header(AUTH) token: String,
    @Path(TRACK_ID) track_id: String,
    @Query(LIMIT) limit: Int): Response<SpotifyTrack>
     **/
}