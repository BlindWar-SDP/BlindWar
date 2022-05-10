package ch.epfl.sdp.blindwar.data.playlists

import androidx.lifecycle.MutableLiveData
import ch.epfl.sdp.blindwar.game.model.Difficulty
import ch.epfl.sdp.blindwar.game.model.Genre
import ch.epfl.sdp.blindwar.game.model.OnlinePlaylist
import ch.epfl.sdp.blindwar.game.model.Playlist
import ch.epfl.sdp.blindwar.game.util.GameUtil
import com.google.firebase.database.FirebaseDatabase

object PlaylistRepository {
    // Playlists cache
    var playlists: MutableLiveData<ArrayList<Playlist>> =
        MutableLiveData(arrayListOf())

    // Remote database reference
    private val remotePlaylistSource =
        FirebaseDatabase.getInstance().getReference("Playlists")

    init {
        playlists = fetchPlaylists()
    }

    /**
     * Retrieve playlists from remote database
     *
     * @param refresh indicates if the cache needs to be refreshed
     * @param query search query by the user
     * @param genreFilter filter by genre (POP, ROCK, ...)
     * @param difficultyFilter filter by difficulty (EASY, MEDIUM, DIFFICULT)
     *
     * @return mutable list of playlists
     */
    private fun fetchPlaylists(
        refresh: Boolean = false,
        query: String = "",
        genreFilter: List<Genre> = Genre.values().toList(),
        difficultyFilter: List<Difficulty> = Difficulty.values().toList()
    ): MutableLiveData<ArrayList<Playlist>> {

        if (refresh || playlists.value?.isEmpty()!!) {
            remotePlaylistSource.get().addOnCompleteListener { task ->
                var response = mutableListOf<Playlist>()
                if (task.isSuccessful) {
                    val result = task.result
                    result?.let {
                        response = result.children.map { snapShot ->
                            snapShot.getValue(OnlinePlaylist::class.java)!!
                        }
                            .filter {
                                it.getName().contains(query)
                                        && genreFilter.containsAll(it.genres)
                                        && difficultyFilter.contains(it.difficulty)
                            }.toMutableList()
                    }
                }

                (response as ArrayList<Playlist>).addAll(GameUtil.BASE_PLAYLISTS)
                playlists.value = (response as ArrayList<Playlist>)
            }
        }

        return playlists
    }
}
