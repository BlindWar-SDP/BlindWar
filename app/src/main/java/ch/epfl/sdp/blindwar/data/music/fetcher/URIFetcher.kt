package ch.epfl.sdp.blindwar.data.music.fetcher

import android.media.MediaPlayer
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata
import ch.epfl.sdp.blindwar.data.music.metadata.URIMusicMetadata

class URIFetcher: Fetcher {
    override fun fetchMusic(musicMetadata: MusicMetadata): Pair<MusicMetadata, MediaPlayer> {
        val player = MediaPlayer()
        player.setDataSource((musicMetadata as URIMusicMetadata).uri)
        player.prepare()

        return Pair(musicMetadata, player)
    }
}