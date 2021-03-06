package ch.epfl.sdp.blindwar.data.music

import android.content.Context
import android.content.res.Resources
import ch.epfl.sdp.blindwar.audio.ReadyMediaPlayer
import ch.epfl.sdp.blindwar.data.music.fetcher.FetcherFactory
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata
import ch.epfl.sdp.blindwar.game.model.Playlist

class MusicRepository(
    resources: Resources,
    context: Context
) {
    // TODO: add Cache and download feature
    private val fetcherFactory: FetcherFactory = FetcherFactory(context, resources)

    /**
     * Fetch musics from ap playlist
     *
     * @param playlist
     * @return
     */
    fun fetchMusics(playlist: Playlist): Map<MusicMetadata, ReadyMediaPlayer> {
        return playlist.songs.associate {
            fetcherFactory.getFetcher(it).fetchMusic(it)
        }
    }
}