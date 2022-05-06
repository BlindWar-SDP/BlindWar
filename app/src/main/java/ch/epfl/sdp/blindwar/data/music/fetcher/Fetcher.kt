package ch.epfl.sdp.blindwar.data.music.fetcher

import ch.epfl.sdp.blindwar.audio.ReadyMediaPlayer
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata

/**
 * Retrieves a music file and returns a player using the file's metadata
 */
interface Fetcher {
    fun fetchMusic(musicMetadata: MusicMetadata): Pair<MusicMetadata, ReadyMediaPlayer>
}