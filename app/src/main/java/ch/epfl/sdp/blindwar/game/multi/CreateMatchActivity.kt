package ch.epfl.sdp.blindwar.game.multi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R

class CreateMatchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_match)

        val nb: NumberPicker = findViewById(R.id.numberOfPLayers)
        nb.maxValue = 20
        nb.minValue = 2
        nb.value = 2
    }

    fun createMatchSoloAttributes(view: View) {

    }

    /**
     * cancel the creation
     *
     * @param view
     */
    fun cancel(view: View) {
        startActivity(Intent(this, MultiPlayerMenuActivity::class.java))
    }
}