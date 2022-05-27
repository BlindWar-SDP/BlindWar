package ch.epfl.sdp.blindwar.data.music.fetcher

import android.content.Context
import android.content.res.Resources
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata

class FetcherFactory(
    private val context: Context,
    private val resources: Resources
) {

    fun getFetcher(musicMetadata: MusicMetadata): Fetcher {
        return if (musicMetadata.uri == null) ResourceFetcher(context, resources) else URIFetcher()
    }
}