package ch.epfl.sdp.blindwar.ui

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.user.AppStatistics
import ch.epfl.sdp.blindwar.user.Mode
import ch.epfl.sdp.blindwar.user.Result
import ch.epfl.sdp.blindwar.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue


class StatisticsActivity : AppCompatActivity() {

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    var userStatistics: AppStatistics = AppStatistics()
    private val userStatsListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val user: User? = try {
                dataSnapshot.getValue<User>()
            } catch (e: DatabaseException) {
                null
            }

            if (user != null) {
                userStatistics = user.userStatistics
            } else {
                userStatistics = AppStatistics()
            }
        }


        override fun onCancelled(databaseError: DatabaseError) {
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (currentUser != null) {
            UserDatabase.addUserListener(currentUser.uid, userStatsListener)
        }
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
                }

            }
        }
    }

}