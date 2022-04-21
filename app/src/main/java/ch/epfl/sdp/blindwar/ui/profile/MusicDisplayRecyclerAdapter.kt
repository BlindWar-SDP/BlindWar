package ch.epfl.sdp.blindwar.ui.profile
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
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
 * @property images
 */
class MusicDisplayRecyclerAdapter(private var titles: List<String>, private var artists: List<String>,
                                  private var imagesURL: List<String>):

RecyclerView.Adapter<MusicDisplayRecyclerAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val itemTitle: TextView = itemView.findViewById(R.id.musicTitle)
        val itemArtist: TextView = itemView.findViewById(R.id.musicArtist)
        val itemPicture: ImageView = itemView.findViewById(R.id.musicImage)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.display_music_cardview,
            parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = titles[position]
        holder.itemArtist.text = artists[position]
        //holder.itemPicture.setImageResource((imagesURL[position]))
        val image = imagesURL[position]
        runBlocking {
            withContext(Dispatchers.IO) {
                try {
                    holder.itemPicture.background = BitmapDrawable(Picasso.get().load(image).get())
                } catch (e: Exception) {
                    holder.itemPicture.background= AppCompatResources.getDrawable(context, R.drawable.logo)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return titles.size
    }
}

