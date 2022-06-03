package ch.epfl.sdp.blindwar.data.music

import android.content.Context
import android.content.res.Resources
import android.os.Build
import ch.epfl.sdp.blindwar.audio.ReadyMediaPlayer
import ch.epfl.sdp.blindwar.data.music.fetcher.FetcherFactory
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata
import ch.epfl.sdp.blindwar.game.model.Playlist
import java.util.stream.Collectors

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
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            playlist.songs.parallelStream()
                .map {
                    fetcherFactory.getFetcher(it).fetchMusic(it)
                }.collect(Collectors.toList()).toMap()
        } else {
            playlist.songs.associate {
                fetcherFactory.getFetcher(it).fetchMusic(it)
            }
        }
    }
}