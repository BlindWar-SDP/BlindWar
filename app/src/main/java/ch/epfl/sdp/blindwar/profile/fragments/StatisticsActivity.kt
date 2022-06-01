package ch.epfl.sdp.blindwar.profile.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.model.config.GameFormat
import ch.epfl.sdp.blindwar.profile.model.AppStatistics
import ch.epfl.sdp.blindwar.profile.viewmodel.ProfileViewModel


// TODO: Remove unused activity
// seems used no ?
class StatisticsActivity : AppCompatActivity() {
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var spinner: Spinner
    private var userStatistics =
        AppStatistics()

    private lateinit var eloView: TextView
    private lateinit var winView: TextView
    private lateinit var drawView: TextView
    private lateinit var lossView: TextView
    private lateinit var winPercent: TextView
    private lateinit var drawPercent: TextView
    private lateinit var lossPercent: TextView
    private lateinit var correctView: TextView
    private lateinit var wrongView: TextView
    private lateinit var correctPercent: TextView
    private lateinit var wrongPercent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        setTextViews()

        // access the spinner
        spinner = findViewById(R.id.modes_spinner)

        // Observe the stats value from the viewModel
        profileViewModel.user.observe(this) {
            if (it.userStatistics != AppStatistics()) {
                userStatistics = it.userStatistics
                setSpinner()
            }
        }
    }

    /**
     * Create spinner for modes
     *
     */
    private fun setSpinner() {
        // access the items of the list
        val modes = GameFormat.values()

        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item, modes
        )
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                Toast.makeText(
                    this@StatisticsActivity,
                    getString(R.string.selected_item) + " " +
                            "" + modes[position], Toast.LENGTH_SHORT
                ).show()

                updateTextViews(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
                Log.d(TAG, "NOTHING SELECTED")
            }
        }
    }

    /**
     * Find views for each attributes of textViews
     *
     */
    private fun setTextViews() {
        eloView = findViewById(R.id.eloExampleView)
        winView = findViewById(R.id.winNumberView)
        drawView = findViewById(R.id.drawNumberView)
        lossView = findViewById(R.id.lossNumberView)
        winPercent = findViewById(R.id.winPercentView)
        drawPercent = findViewById(R.id.drawPercentView)
        lossPercent = findViewById(R.id.lossPercentView)
        correctView = findViewById(R.id.correctNumberView)
        wrongView = findViewById(R.id.wrongNumberView)
        correctPercent = findViewById(R.id.correctnessPercentView)
        wrongPercent = findViewById(R.id.wrongPercentView)
    }

    /**
     * Update the text views
     *
     * @param position
     */
    private fun updateTextViews(position: Int) {
        eloView.text = userStatistics.elo.toString()
        winView.text = userStatistics.wins[position].toString()
        drawView.text = userStatistics.draws[position].toString()
        lossView.text = userStatistics.losses[position].toString()
        winPercent.text = getPercentStringFormatted(userStatistics.winPercent[position])
        drawPercent.text = getPercentStringFormatted(userStatistics.drawPercent[position])
        lossPercent.text = getPercentStringFormatted(userStatistics.lossPercent[position])
        correctView.text = getPercentStringFormatted(userStatistics.correctArray[position])
        wrongView.text = getPercentStringFormatted(userStatistics.wrongArray[position])
        correctPercent.text = getPercentStringFormatted(userStatistics.correctPercent[position])
        wrongPercent.text = getPercentStringFormatted(userStatistics.wrongPercent[position])
    }

    /**
     * Get a Value (arg) followed by % character
     *
     * @param arg
     * @return
     */
    private fun <T> getPercentStringFormatted(arg: T): String {
        return getString(R.string.percent).format(arg)
    }
}