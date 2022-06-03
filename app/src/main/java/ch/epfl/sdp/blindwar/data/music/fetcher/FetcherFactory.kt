package ch.epfl.sdp.blindwar.data.music.fetcher

import android.content.Context
import android.content.res.Resources
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata

class FetcherFactory(
    private val context: Context,
    private val resources: Resources
) {
    /**
     * Return a fetcher depending on the uri of musicMetadata arg
     *
     * @param musicMetadata
     * @return
     */
    fun getFetcher(musicMetadata: MusicMetadata): Fetcher {
        return if (musicMetadata.uri == null) ResourceFetcher(context, resources) else URIFetcher()
    }
}