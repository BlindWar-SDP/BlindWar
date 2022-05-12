package ch.epfl.sdp.blindwar.profile.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindwar.R

class MatchHistoryRecyclerAdapter(
    private var victories: List<String>,
    private var gameMode: List<String>,
    private var rounds: List<String>,
    private var scores: List<String>
) :

    RecyclerView.Adapter<MatchHistoryRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val result: TextView = itemView.findViewById(R.id.userRank)
        val gameMode: TextView = itemView.findViewById(R.id.userPseudo)
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