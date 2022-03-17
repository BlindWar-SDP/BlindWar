package ch.epfl.sdp.blindwar.ui.tutorial

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Base64.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ch.epfl.sdp.blindwar.domain.game.GameTutorial
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.data.SpotifyApiConstants
import ch.epfl.sdp.blindwar.data.SpotifyApiConstants.AUTH_TYPE
import ch.epfl.sdp.blindwar.data.SpotifyApiConstants.CLIENT_ID
import ch.epfl.sdp.blindwar.data.SpotifyApiConstants.CLIENT_SECRET
import ch.epfl.sdp.blindwar.data.SpotifyService
import ch.epfl.sdp.blindwar.data.SpotifyToken
import com.chibde.visualizer.LineVisualizer
import com.squareup.picasso.Picasso
import retrofit2.HttpException
import java.io.IOException

class DemonstrationTutorialFragment : Fragment() {
    private lateinit var gameTutorial: GameTutorial
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View =
            inflater.inflate(R.layout.fragment_demonstration_tutorial, container, false)
        val btn: Button = view.findViewById(R.id.button)

        gameTutorial = activity?.applicationContext?.let { GameTutorial(it.assets) }!!

        btn.setOnClickListener {
            gameTutorial.nextRound()
        }

        /**
        val lineVisualizer2: LineVisualizer = view.findViewById(R.id.visualizer)

        // Set your media player to the visualizer.
        lineVisualizer2.setPlayer(gameTutorial.sessionId)
        **/

        val lineVisualizer: LineVisualizer = view.findViewById(R.id.visualizer)
        lineVisualizer.setStrokeWidth(10)

        // Set your media player to the visualizer.
        lineVisualizer.setPlayer(gameTutorial.sessionId)

        val artistView = view.findViewById<ImageView>(R.id.artist)
        var logged: Boolean = false

        lifecycleScope.launchWhenCreated {
            var responseToken: SpotifyToken = SpotifyToken("", 0, "")

            /* Get access token */
            if (!logged) {
                val auth = try {
                    val credentials = "Basic ${encodeToString("$CLIENT_ID:$CLIENT_SECRET".toByteArray(Charsets.UTF_8), NO_WRAP)}"
                    Log.d(TAG, credentials)
                    SpotifyService.apiAuth.value.getToken(credentials, AUTH_TYPE)
                } catch(e: IOException) {
                    Log.e(TAG, "IOException")
                    return@launchWhenCreated
                } catch (e: HttpException) {
                    Log.e(TAG, "HttpException")
                    return@launchWhenCreated
                } catch (e: Exception) {
                    Log.d(TAG, e.stackTraceToString())
                    return@launchWhenCreated
                }

                if (auth.isSuccessful && auth.body() != null) {
                    Log.d(TAG, responseToken.toString())
                    responseToken = auth.body()!!
                    logged = true
                    Log.d(TAG, "AUTHENTICATION SUCCESSFUL")
                    Log.d(TAG, responseToken.access_token)
                } else {
                    Log.d(TAG, "AUTHENTICATION NOT SUCCESSFUL ${auth.code().toString()+auth.message()+auth.toString()}")
                }
            }

            /* Prefetch metadata */
            if (logged) {
                // Hard coded until the token retrieval is resolved
                //responseToken.access_token = "BQD9fdu-CEOGxQOR7A5HohOIIqZY655mUaVx_zgvfluJfqx_NZlbrnJuSp3GZxZg-e6XKJUFoELwVLngUVxrl4Eno8bybSM9LASeA4ltfTD-1MJ1yQKxKAurykv7TY9sz6fRVMg_Z-bfDhgD"
                    // Bearer ${responseToken.access_token}
                val response = try {
                    SpotifyService.apiMeta.value.getArtist("Bearer ${responseToken.access_token}", "3fMbdgg4jU18AjLCKBhRSm")
                } catch(e: IOException) {
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
                    Picasso.get().load(imageUrl).into(artistView)
                } else {
                    Log.e(TAG, "Response not successful")
                }
            }
        }

        return view
    }
}