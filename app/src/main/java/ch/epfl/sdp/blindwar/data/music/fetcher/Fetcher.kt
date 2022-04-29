package ch.epfl.sdp.blindwar.data.music.fetcher

import android.media.MediaPlayer
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata

/**
 * Retrieves a music file and returns a player using the file's metadata
 */
interface Fetcher {
    fun fetchMusic(musicMetadata: MusicMetadata): Pair<MusicMetadata, MediaPlayer>
}