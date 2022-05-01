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
    @RequiresApi(Build.VERSION_CODES.N)
    fun fetchMusics(playlist: Playlist): Map<MusicMetadata, ReadyMediaPlayer> {
        return playlist.songs.parallelStream()
            .map {
                fetcherFactory.getFetcher(it).fetchMusic(it)
            }.collect(Collectors.toList()).toMap().also {
                it.values.forEach{ mp ->
                    mp.mediaPlayer.setOnPreparedListener{
                        mp.ready.postValue(true)
                        val time = Random().nextInt(30000)

                        // Change the current music
                        mp.mediaPlayer.seekTo(time)
                    }

                    mp.mediaPlayer.prepareAsync()
                }
            }
    }
}