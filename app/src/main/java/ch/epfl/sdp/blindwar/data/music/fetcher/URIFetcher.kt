package ch.epfl.sdp.blindwar.data.music.fetcher

import android.content.Context
import android.net.Uri

abstract class URIFetcher(protected val uri: Uri, context: Context): Fetcher(context) {
}