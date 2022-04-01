package ch.epfl.sdp.blindwar.ui

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.user.Mode


class StatisticsActivity : AppCompatActivity() {

    private val test = 59




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        // access the items of the list
        val modes = Mode.values()


        // access the spinner
        val spinner = findViewById<Spinner>(R.id.modes_spinner)
        if (spinner != null) {
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

                    val textView = findViewById<View>(R.id.winNumberView) as TextView
                    textView.text = modes[position].toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }


}