package ch.epfl.sdp.blindwar.data.music.fetcher

import android.content.Context
import android.net.Uri

abstract class URIMusicReference(protected val uri: Uri, context: Context): MusicReference(context) {
}