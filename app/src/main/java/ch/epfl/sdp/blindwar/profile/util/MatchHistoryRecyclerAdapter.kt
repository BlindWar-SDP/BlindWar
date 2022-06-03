package ch.epfl.sdp.blindwar.profile.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindwar.R

class MatchHistoryRecyclerAdapter(
    private var victories: List<String>,
    private var gameTimes: List<String>,
    private var gameMode: List<String>,
    private var rounds: List<String>,
    private var scores: List<String>
) : RecyclerView.Adapter<MatchHistoryRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gameResultWin: TextView = itemView.findViewById(R.id.gameResultWin)
        val gameResultLoss: TextView = itemView.findViewById(R.id.gameResultLoss)
        val gameMode: TextView = itemView.findViewById(R.id.gameMode)
        val gameRounds: TextView = itemView.findViewById(R.id.gameRounds)
        val gameScore: TextView = itemView.findViewById(R.id.gameScore)
        val gameTime: TextView = itemView.findViewById(R.id.gameTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.match_history_cardview,
            parent, false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (victories[position] == "WIN") {
            holder.gameResultWin.text = victories[position]
        } else {
            holder.gameResultLoss.text = victories[position]
        }
        holder.gameMode.text = gameMode[position]
        holder.gameRounds.text = rounds[position]
        holder.gameScore.text = scores[position]
        holder.gameTime.text = gameTimes[position]
    }

    override fun getItemCount(): Int {
        return victories.size
    }
}