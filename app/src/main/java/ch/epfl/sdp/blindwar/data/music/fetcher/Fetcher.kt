package ch.epfl.sdp.blindwar.data.music.fetcher

import android.media.MediaPlayer
import ch.epfl.sdp.blindwar.data.music.MusicMetadata

interface Fetcher {
    fun fetchMusic(musicMetadata: MusicMetadata): Pair<MusicMetadata, MediaPlayer>
}