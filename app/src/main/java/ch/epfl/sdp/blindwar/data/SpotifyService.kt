package ch.epfl.sdp.blindwar.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val SPOTIFY_META_END_POINT = "https://api.spotify.com/v1/"
const val SPOTIFY_AUTH_END_POINT = "https://accounts.spotify.com/"

object SpotifyService {
    private fun spotifyApiFactory(endpoint: String): Lazy<SpotifyApi> {
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