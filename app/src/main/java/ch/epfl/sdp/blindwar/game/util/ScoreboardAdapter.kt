package ch.epfl.sdp.blindwar.game.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindwar.R

class ScoreboardAdapter(private var playersName: List<String>)  :
    RecyclerView.Adapter<ScoreboardAdapter.ViewHolder>() {
    private var dataSet = playersName.map { Pair(0, it) }.toMutableList()

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val numberOfPointTextView: TextView = view.findViewById(R.id.row_number_point)
        val nameTextView: TextView = view.findViewById(R.id.row_name)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.scoreboard_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val sortedDataSet = dataSet.sortedBy { it.first }.reversed()
        viewHolder.numberOfPointTextView.text = sortedDataSet[position].first.toString()
        viewHolder.nameTextView.text = sortedDataSet[position].second
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    /**
     * Increment the number of point of a player in the scoreboard
     *
     * @param playerName Name of the player
     * @return
     */
    fun incrementPoint(playerName: String) {
        dataSet?.indexOfFirst { it.second == playerName }.let { dataSet?.set(it!!, Pair(
            dataSet!![it].first + 1, playerName))}
    }
}