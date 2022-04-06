package ch.epfl.sdp.blindwar.data.music

object MusicController {
    fun fetchMusics(playlist: Playlist) {
        playlist.fetchers.forEach {it.fetch()}
    }
}