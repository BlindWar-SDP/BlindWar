package ch.epfl.sdp.blindwar.data.music

import android.content.Context
import android.content.res.Resources
import android.media.MediaPlayer
import ch.epfl.sdp.blindwar.data.music.fetcher.FetcherFactory
import ch.epfl.sdp.blindwar.game.model.OnlinePlaylist
import ch.epfl.sdp.blindwar.game.model.Playlist

class MusicRepository(
    resources: Resources,
    context: Context
) {

    // TODO: add Cache and download feature
    private val fetcherFactory: FetcherFactory = FetcherFactory(context, resources)
    fun fetchMusics(playlist: Playlist): Map<MusicMetadata, MediaPlayer> =
        playlist.songs.associate { fetcherFactory.getFetcher(it).fetchMusic(it) }
}