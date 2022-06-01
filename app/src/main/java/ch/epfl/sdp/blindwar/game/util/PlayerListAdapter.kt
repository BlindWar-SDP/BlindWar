package ch.epfl.sdp.blindwar.game.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindwar.R

class PlayerListAdapter(private var dataSet: List<Pair<Int, String>>) :
    RecyclerView.Adapter<PlayerListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val numberOfPointTextView: TextView = view.findViewById(R.id.row_number_point)
        val nameTextView: TextView = view.findViewById(R.id.row_name)
    }

    /**
     * Create new views (invoked by the layout manager)
     *
     * @param viewGroup
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.player_row_item, viewGroup, false)
        return ViewHolder(view)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     *
     * @param viewHolder
     * @param position
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val sortedDataSet = dataSet.sortedBy { it.first }.reversed()
        viewHolder.numberOfPointTextView.text = sortedDataSet[position].first.toString()
        viewHolder.nameTextView.text = sortedDataSet[position].second
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     *
     * @return
     */
    override fun getItemCount() = dataSet.size

}