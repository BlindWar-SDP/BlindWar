package ch.epfl.sdp.blindwar.data.music.fetcher

import android.content.Context
import android.content.res.Resources
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import ch.epfl.sdp.blindwar.data.music.MusicImageUrlConstants
import ch.epfl.sdp.blindwar.data.music.MusicMetadata
import ch.epfl.sdp.blindwar.data.music.ResourceMusicMetadata
import ch.epfl.sdp.blindwar.data.music.URIMusicMetadata
import java.util.*

interface Fetcher {
    fun fetchMusic(musicMetadata: MusicMetadata): Pair<MusicMetadata, MediaPlayer>
}