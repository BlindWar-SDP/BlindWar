package ch.epfl.sdp.blindwar.ui.tutorial

//import com.squareup.picasso.Picasso
//import retrofit2.HttpException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.data.SpotifyToken
import ch.epfl.sdp.blindwar.domain.game.GameTutorial

/** Fragment used to test metadata fetching through Spotify Api
 * soon to be refactored using a clean architecture **/
class DemonstrationTutorialFragment : Fragment() {
    private lateinit var gameTutorial: GameTutorial

    //private var logged: Boolean = false
    private lateinit var responseToken: SpotifyToken
    private lateinit var artistView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View =
            inflater.inflate(R.layout.fragment_demonstration_tutorial, container, false)
        val btn: Button = view.findViewById(R.id.button)

        gameTutorial = activity?.applicationContext?.let { GameTutorial(it.assets, 5000) }!!

        btn.setOnClickListener {
            gameTutorial.nextRound()
        }

        artistView = view.findViewById(R.id.artist)
        responseToken = SpotifyToken("", 0, "")

        //fetchToken()

        return view
    }
/*
    private fun fetchToken() {
        lifecycleScope.launchWhenCreated {
            if (!logged) {
                val auth = try {
                    val credentials = credentialsEncoding()
                    Log.d(TAG, credentials)
                    SpotifyService.apiAuth.value.getToken(credentials, AUTH_TYPE)
                } catch (e: IOException) {
                    return@launchWhenCreated
                } catch (e: HttpException) {
                    return@launchWhenCreated
                } catch (e: Exception) {
                    return@launchWhenCreated
                }

                if (auth.isSuccessful && auth.body() != null) {
                    Log.d(TAG, responseToken.toString())
                    responseToken = auth.body()!!
                    logged = true
                    Log.d(TAG, "AUTHENTICATION SUCCESSFUL")
                    Log.d(TAG, responseToken.access_token)
                    fetchMetadata()
                } else {
                    Log.d(
                        TAG,
                        "AUTHENTICATION NOT SUCCESSFUL ${
                            auth.code().toString() + auth.message() + auth.toString()
                        }"
                    )
                }
            }
        }
    }

    private fun fetchMetadata() {
        lifecycleScope.launchWhenCreated {
            if (logged) {
                val response = try {
                    SpotifyService.apiMeta.value.getArtist(
                        "Bearer ${responseToken.access_token}",
                        "3fMbdgg4jU18AjLCKBhRSm"
                    )
                } catch (e: IOException) {
                    Log.e(TAG, "IOException, you might not have internet connection")
                    logged = false
                    return@launchWhenCreated
                } catch (e: HttpException) {
                    Log.e(TAG, "HttpException, unexpected response")
                    logged = false
                    return@launchWhenCreated
                }

                if (response.isSuccessful && response.body() != null) {
                    val imageUrl = response.body()!!.images[0].url
                    // Put the image in the cache
                    Picasso.get().load(imageUrl).into(artistView)
                    //songMetaData = spotifyArtistToSongMetadata(response.body()!!)
                } else {
                    Log.e(TAG, "Response not successful")
                }
            }
        }
    }*/
}