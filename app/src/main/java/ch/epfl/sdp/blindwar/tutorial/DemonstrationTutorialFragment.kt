package ch.epfl.sdp.blindwar.tutorial

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ch.epfl.sdp.blindwar.game.GameTutorial
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.meta.RetrofitInstance
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

        val lineVisualizer2: LineVisualizer = view.findViewById(R.id.visualizer)

        // Set your media player to the visualizer.
        lineVisualizer2.setPlayer(gameTutorial.sessionId)

        val lineVisualizer: LineVisualizer = view.findViewById(R.id.visualizerLine)

        // Set your media player to the visualizer.
        lineVisualizer.setPlayer(gameTutorial.sessionId)

        lifecycleScope.launchWhenCreated {
            val response = try {
                RetrofitInstance.api.getArtist()
            } catch(e: IOException) {
                Log.e(TAG, "IOException, you might not have internet connection")
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                return@launchWhenCreated
            }

            if(response.isSuccessful && response.body() != null) {
                val artistView = view.findViewById<ImageView>(R.id.artist)
                val imageUrl = response.body()!!.images[0].url
                Picasso.get().load(imageUrl).into(artistView)
            } else {
                Log.e(TAG, "Response not successful")
            }
        }

        return view
    }
}