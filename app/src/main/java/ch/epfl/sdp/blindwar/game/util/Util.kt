package ch.epfl.sdp.blindwar.game.util

import android.os.CountDownTimer
import android.widget.Filter
import ch.epfl.sdp.blindwar.game.model.OnlinePlaylist
import ch.epfl.sdp.blindwar.game.model.Playlist
import com.google.android.material.progressindicator.CircularProgressIndicator
import java.util.*

object Util {
    /**
     * Create a countdown timer
     * @param duration timer's duration
     * @param progress visual indicator reference
     */
    fun createCountDown(duration: Long, progress: CircularProgressIndicator): CountDownTimer {
        return object : CountDownTimer(duration, 10) {

            override fun onTick(millisUntilFinished: Long) {
                progress.progress =
                    (((duration.toDouble() - millisUntilFinished.toDouble()) / duration.toDouble()) * 100).toInt()
            }

            override fun onFinish() {}
        }
    }

    /** TODO: Add generic parameter once we use a Song Recycler View **/
    /**
     * Creates a Filter based on the name for the playlistSelectionFragment
     * @param initialOnlinePlaylists base list of playlists
     * @param onlinePlaylistSet list of available playlists
     * @param playlistAdapter playlistRecyclerView adapter
     */
    fun playlistFilterQuery(initialOnlinePlaylists: List<Playlist>,
                            onlinePlaylistSet: ArrayList<Playlist>,
                            playlistAdapter: PlaylistAdapter
    ): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: ArrayList<Playlist> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                initialOnlinePlaylists.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().lowercase(Locale.getDefault())
                initialOnlinePlaylists.forEach {
                    if (it.name.lowercase(Locale.getDefault()).contains(query)) {
                        filteredList.add(it)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results?.values is ArrayList<*>) {
                onlinePlaylistSet.clear()
                onlinePlaylistSet.addAll(results.values as ArrayList<Playlist>)
                playlistAdapter.notifyDataSetChanged()
            }
        }
    }
}