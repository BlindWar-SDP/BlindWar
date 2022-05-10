package ch.epfl.sdp.blindwar.game.multi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.model.config.GameFormat
import ch.epfl.sdp.blindwar.game.util.GameActivity

class ChoseNumberOfPlayerActivity : AppCompatActivity(){

    companion object {
        const val DEFAULT_NUMBER_OF_PLAYER = 2
        const val MINIMUM_NUMBER_OF_PLAYER = 2
        const val MAXIMUM_NUMBER_OF_PLAYER = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chose_number_of_player)

        val nb: NumberPicker = findViewById(R.id.number_of_players)
        nb.maxValue = MAXIMUM_NUMBER_OF_PLAYER
        nb.minValue = MINIMUM_NUMBER_OF_PLAYER
        nb.value = DEFAULT_NUMBER_OF_PLAYER
    }

    fun createMatchSoloAttributes(view: View) {
        val nb: NumberPicker = findViewById(R.id.number_of_players)
        val checkBox: CheckBox = findViewById(R.id.checkBoxIsPrivate)

        // TODO: Do not ignore these data !
        val maxPlayer = nb.value
        val isPrivate = checkBox.isChecked

        startActivity(Intent(this, GameActivity::class.java).apply { putExtra(GameActivity.GAME_FORMAT_EXTRA_NAME, GameFormat.MULTI) })
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