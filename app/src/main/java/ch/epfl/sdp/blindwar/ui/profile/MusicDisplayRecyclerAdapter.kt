package ch.epfl.sdp.blindwar.ui.profile
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindwar.R

/**
 * Class to handle the RecyclerView used to display the liked music of a user
 *
 * @property titles
 * @property artists
 * @property images
 */
class MusicDisplayRecyclerAdapter(private var titles: List<String>, private var artists: List<String>, private var images: List<String>):

RecyclerView.Adapter<MusicDisplayRecyclerAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val itemTitle: TextView = itemView.findViewById(R.id.musicTitle)
        val itemArtist: TextView = itemView.findViewById(R.id.musicArtist)
        val itemPicture: TextView = itemView.findViewById(R.id.musicImage)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.display_music_cardview,
            parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = titles[position]
        holder.itemArtist.text = artists[position]
        //holder.itemPicture.setImageRessource(images)
    }

    override fun getItemCount(): Int {
        return titles.size
    }
}

}