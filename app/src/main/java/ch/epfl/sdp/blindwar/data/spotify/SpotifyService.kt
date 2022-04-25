package ch.epfl.sdp.blindwar.data.spotify

import ch.epfl.sdp.blindwar.data.spotify.SpotifyApiConstants.SPOTIFY_AUTH_END_POINT
import ch.epfl.sdp.blindwar.data.spotify.SpotifyApiConstants.SPOTIFY_META_END_POINT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object SpotifyService {
    fun spotifyApiFactory(endpoint: String): Lazy<SpotifyApi> {
        return lazy {

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()


            Retrofit.Builder()
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(SpotifyApi::class.java)
        }
    }

    val apiMeta = spotifyApiFactory(SPOTIFY_META_END_POINT)
    val apiAuth = spotifyApiFactory(SPOTIFY_AUTH_END_POINT)
}