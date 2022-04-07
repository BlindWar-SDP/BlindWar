package ch.epfl.sdp.blindwar.data.music

import android.media.MediaPlayer

object MusicController {
    fun fetchMusics(playlist: Playlist): Map<MusicMetadata, MediaPlayer> = playlist.fetchers.map { Pair(it, it.getMediaPlayer()) }.associateBy( { it.first.musicMetadata }, {it.second})
    /*{
        playlist.fetchers.forEach {it.fetch()}
        playlist.fetchers.map { Pair(it, it.fetch()) }.associateBy( { it.first.musicMetadata }, {it.second})
    }
     */
}