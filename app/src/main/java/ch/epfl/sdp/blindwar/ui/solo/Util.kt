package ch.epfl.sdp.blindwar.ui.solo

import android.os.CountDownTimer
import android.widget.Filter
import com.google.android.material.progressindicator.CircularProgressIndicator
import java.util.*

object Util {
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
    fun playlistFilterQuery(initialPlaylists: List<PlaylistModel>,
                    playlistModelSet: ArrayList<PlaylistModel>,
                    playlistAdapter: PlaylistAdapter): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: ArrayList<PlaylistModel> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                initialPlaylists.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().lowercase(Locale.getDefault())
                initialPlaylists.forEach {
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
                playlistModelSet.clear()
                playlistModelSet.addAll(results.values as ArrayList<PlaylistModel>)
                playlistAdapter.notifyDataSetChanged()
            }
        }
    }
}