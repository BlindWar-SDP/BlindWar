package ch.epfl.sdp.blindwar.data.music

import android.content.Context
import android.content.res.Resources
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import ch.epfl.sdp.blindwar.audio.ReadyMediaPlayer
import ch.epfl.sdp.blindwar.data.music.fetcher.FetcherFactory
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata
import ch.epfl.sdp.blindwar.game.model.Playlist
import java.util.*
import java.util.stream.Collectors

class MusicRepository(
    resources: Resources,
    context: Context
) {
    // TODO: add Cache and download feature
    private val fetcherFactory: FetcherFactory = FetcherFactory(context, resources)
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