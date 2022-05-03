package ch.epfl.sdp.blindwar.profile.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.profile.model.AppStatistics
import ch.epfl.sdp.blindwar.profile.model.Mode
import ch.epfl.sdp.blindwar.profile.viewmodel.ProfileViewModel


// TODO: Remove unused activity
class StatisticsActivity: AppCompatActivity() {
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var spinner: Spinner
    private var userStatistics =
        AppStatistics()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        // access the spinner
        spinner = findViewById(R.id.modes_spinner)

        // Observe the stats value from the viewModel
        profileViewModel.userStatistics.observe(this) {
            if (it != AppStatistics()) {
                userStatistics = it
                setSpinner()
            }
        }
    }

    private fun setSpinner() {
        // access the items of the list
        val modes = Mode.values()

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

                val eloView = findViewById<TextView>(R.id.eloExampleView)
                val winView = findViewById<TextView>(R.id.winNumberView)
                val drawView = findViewById<TextView>(R.id.drawNumberView)
                val lossView = findViewById<TextView>(R.id.lossNumberView)
                val winPercent = findViewById<TextView>(R.id.winPercentView)
                val drawPercent = findViewById<TextView>(R.id.drawPercentView)
                val lossPercent = findViewById<TextView>(R.id.lossPercentView)
                val correctView = findViewById<TextView>(R.id.correctNumberView)
                val wrongView = findViewById<TextView>(R.id.wrongNumberView)
                val correctPercent = findViewById<TextView>(R.id.correctnessPercentView)
                val wrongPercent = findViewById<TextView>(R.id.wrongPercentView)

                /*
                userStatistics.eloUpdate(Result.WIN, 1300)
                userStatistics.multiWinLossCountUpdate(Result.WIN, Mode.MULTI)
                userStatistics.correctnessUpdate(1, 0, Mode.SOLO)
                */

                eloView.text = userStatistics.elo.toString()
                winView.text = userStatistics.wins[position].toString()
                drawView.text = userStatistics.draws[position].toString()
                lossView.text = userStatistics.losses[position].toString()
                winPercent.text = userStatistics.winPercent[position].toString()
                drawPercent.text = userStatistics.drawPercent[position].toString()
                lossPercent.text = userStatistics.lossPercent[position].toString()
                correctView.text = userStatistics.correctArray[position].toString()
                wrongView.text = userStatistics.wrongArray[position].toString()
                correctPercent.text = userStatistics.correctPercent[position].toString()
                wrongPercent.text = userStatistics.wrongPercent[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
                Log.d(TAG, "NOTHING SELECTED")
            }
        }
    }
}