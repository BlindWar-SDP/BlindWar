package ch.epfl.sdp.blindwar.data

import ch.epfl.sdp.blindwar.data.SpotifyApi
import ch.epfl.sdp.blindwar.data.SpotifyApiConstants.SPOTIFY_AUTH_END_POINT
import ch.epfl.sdp.blindwar.data.SpotifyApiConstants.SPOTIFY_META_END_POINT
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



object SpotifyService {
    fun spotifyApiFactory(endpoint: String): Lazy<SpotifyApi> {
        return lazy {
            /** Only for debugging purposes
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient: OkHttpClient = Builder()
            .addInterceptor(loggingInterceptor)
            .build()
             **/

            Retrofit.Builder()
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(okHttpClient)
                .build()
                .create(SpotifyApi::class.java)
        }
    }

    val apiMeta = spotifyApiFactory(SPOTIFY_META_END_POINT)
    val apiAuth = spotifyApiFactory(SPOTIFY_AUTH_END_POINT)
}