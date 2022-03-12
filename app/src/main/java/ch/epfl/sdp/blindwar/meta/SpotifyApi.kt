package ch.epfl.sdp.blindwar.meta

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface SpotifyApi {
    @GET("artists/3fMbdgg4jU18AjLCKBhRSm")
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "Authorization: Bearer BQCjrsqSD2HRy_P8RilNdWhzjIfX7j6NzGcXR-tbj7kQjWKfw95jY7aHyrMRSTMnaDocs6d_qF372cq6IPwiP_R9YxSPgZ-83hitYvRxkuBm8fBrW9yiYiVNJcZyyYx4zivGfBdXpa1vXIxF"
    )
    suspend fun getArtist()
    : Response<SpotifyArtist>

    @GET("artists/3fMbdgg4jU18AjLCKBhRSm")
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "Authorization: Bearer BQCjrsqSD2HRy_P8RilNdWhzjIfX7j6NzGcXR-tbj7kQjWKfw95jY7aHyrMRSTMnaDocs6d_qF372cq6IPwiP_R9YxSPgZ-83hitYvRxkuBm8fBrW9yiYiVNJcZyyYx4zivGfBdXpa1vXIxF"
    )
    suspend fun searchArtist()
            : Response<SpotifyArtist>
}