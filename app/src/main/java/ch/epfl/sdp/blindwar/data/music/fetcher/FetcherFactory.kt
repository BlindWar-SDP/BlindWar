package ch.epfl.sdp.blindwar.data.music.fetcher

import android.content.Context
import android.content.res.Resources
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata
import ch.epfl.sdp.blindwar.data.music.metadata.URIMusicMetadata

class FetcherFactory(
    private val context: Context,
    private val resources: Resources
) {

    fun getFetcher(musicMetadata: MusicMetadata): Fetcher {
        return if (musicMetadata is URIMusicMetadata) URIFetcher()
        else ResourceFetcher(context, resources)
    }
}