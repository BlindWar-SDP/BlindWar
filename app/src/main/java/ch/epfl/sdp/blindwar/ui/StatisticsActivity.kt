package ch.epfl.sdp.blindwar.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R


class StatisticsActivity : AppCompatActivity() {

    //Float values for stats (only for demo)
    private var wins = 45
    private var losses = 35
    private var draws = 100 - wins - losses


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        // access the items of the list
        val modes = resources.getStringArray(R.array.modes_array)

        // access the spinner
        val spinner = findViewById<Spinner>(R.id.modes_spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                R.layout.spinner_item, modes)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    Toast.makeText(this@StatisticsActivity,
                        getString(R.string.selected_item) + " " +
                                "" + modes[position], Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }




}