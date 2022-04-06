package ch.epfl.sdp.blindwar.data.music

import android.content.Context
import android.content.res.Resources
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.provider.MediaStore
import ch.epfl.sdp.blindwar.ui.solo.PlaylistModel

class MusicController(private val resources: Resources,
                      private val context: Context
) {
    private val fetcherFactory: FetcherFactory = FetcherFactory(context, resources)
    fun fetchMusics(playlist: PlaylistModel): Map<MusicMetadata, MediaPlayer> =
        playlist.songs.associate { fetcherFactory.getFetcher(it).fetchMusic(it) }
    // playlist.songs.map { fetchMusic(it) }

    /*{
        playlist.fetchers.forEach {it.fetch()}
        playlist.fetchers.map { Pair(it, it.fetch()) }.associateBy( { it.first.musicMetadata }, {it.second})
    }
     */
}