package ch.epfl.sdp.blindwar.profile.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindwar.R

/**
 * Class to handle the RecyclerView used to display a leaderboard with rank, pseudo and elo of
 * each User.
 *
 * @property ranks
 * @property pseudos
 * @property elos
 * @property wins
 * @property losses
 */
class LeaderboardRecyclerAdapter(
    private var ranks: List<String>,
    private var pseudos: List<String>,
    private var elos: List<String>,
    private var wins: List<String>,
    private var losses: List<String>
) :

    RecyclerView.Adapter<LeaderboardRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userRank: TextView = itemView.findViewById(R.id.userRank)
        val userPseudo: TextView = itemView.findViewById(R.id.userPseudo)
        val userElo: TextView = itemView.findViewById(R.id.userElo)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.leaderboard_cardview,
            parent, false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Add an header to describe the data of the leaderboard
        if (position == 0) {
            holder.userRank.text = "Rank"
            holder.userPseudo.text = "Name"
            holder.userElo.text = "Wins/Losses"
        } else {
            holder.userRank.text = '#' + (position).toString()
            holder.userPseudo.text = pseudos[position - 1]
            holder.userElo.text =
                "W:" + wins[position - 1] + "/L:" + losses[position - 1] + "  elo: " +
                        elos[position - 1]
        }
    }

    override fun getItemCount(): Int {
        return ranks.size
    }

}