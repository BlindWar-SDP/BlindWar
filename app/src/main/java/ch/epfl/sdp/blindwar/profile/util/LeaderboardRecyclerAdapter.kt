package ch.epfl.sdp.blindwar.profile.util

import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindwar.R
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * Class to handle the RecyclerView used to display the liked music of a user
 *
 * @property titles
 * @property artists
 * @property imagesURL
 */
class LeaderboardRecyclerAdapter(
    private var ranks: List<String>, private var pseudos: List<String>,
    private var elos: List<String>
) :

    RecyclerView.Adapter<LeaderboardRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userRank: TextView = itemView.findViewById(R.id.musicTitle)
        val userPseudo: TextView = itemView.findViewById(R.id.musicArtist)
        val userElo: ImageView = itemView.findViewById(R.id.musicImage)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.leaderboard_cardview,
            parent, false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.userRank.text = titles[position]
        holder.userPseudo.text = artists[position]
        holder.userElo.text =
        //holder.itemPicture.setImageResource((imagesURL[position]))

    }

    override fun getItemCount(): Int {
        return titles.size
    }
}