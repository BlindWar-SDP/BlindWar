package ch.epfl.sdp.blindwar.data

import android.media.MediaPlayer
import android.net.Uri

interface Fetcher {
    fun fetch(uri: Uri): MediaPlayer
    fun fetch(urls: SpotifyExternalUrls): MediaPlayer
}