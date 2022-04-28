/*
package ch.epfl.sdp.blindwar.profile.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.profile.model.AppStatistics
import ch.epfl.sdp.blindwar.profile.model.Mode
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue


/**
 * Fragment that displays the user game statistics
 * TODO: fix CodeClimate issues / Cirrus warnings
 *
 * @constructor creates a StatisticsFragment
 */
class StatisticsFragment : Fragment() {
    //private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    var userStatistics: AppStatistics = AppStatistics()
    private val userStatsListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val user: User? = try {
                dataSnapshot.getValue<User>()
            } catch (e: DatabaseException) {
                null
            }

            userStatistics = user?.userStatistics ?: AppStatistics()
        }


        override fun onCancelled(databaseError: DatabaseError) {
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.activity_statistics, container, false)

        super.onCreate(savedInstanceState)
        if (currentUser != null) {
            UserDatabase.addUserListener(currentUser.uid, userStatsListener)
        }

        // access the items of the list
        val modes = Mode.values()

        // access the spinner
        val spinner = view.findViewById<Spinner>(R.id.modes_spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                requireContext(),
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
                        requireContext(),
                        getString(R.string.selected_item) + " " +
                                "" + modes[position], Toast.LENGTH_SHORT
                    ).show()
                    val eloView = view.findViewById<TextView>(R.id.eloExampleView)
                    val winView = view.findViewById<TextView>(R.id.winNumberView)
                    val drawView = view.findViewById<TextView>(R.id.drawNumberView)
                    val lossView = view.findViewById<TextView>(R.id.lossNumberView)
                    val winPercent = view.findViewById<TextView>(R.id.winPercentView)
                    val drawPercent = view.findViewById<TextView>(R.id.drawPercentView)
                    val lossPercent = view.findViewById<TextView>(R.id.lossPercentView)
                    val correctView = view.findViewById<TextView>(R.id.correctNumberView)
                    val wrongView = view.findViewById<TextView>(R.id.wrongNumberView)
                    val correctPercent = view.findViewById<TextView>(R.id.correctnessPercentView)
                    val wrongPercent = view.findViewById<TextView>(R.id.wrongPercentView)
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

        return view
    }
}*/