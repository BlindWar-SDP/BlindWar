package ch.epfl.sdp.blindwar.data.music

import android.net.Uri

abstract class MusicMetadata(val title: String,
                         val artist: String,
                         val imageUrl: String? = null,
                         val duration: Int) {
    override fun toString(): String = "$title by $artist"
}

class URIMusicMetadata(title: String = "title",
                       artist: String = "artist",
                       imageUrl: String?,
                       duration: Int = 0,
                       val uri: String = "uri"): MusicMetadata(title, artist, imageUrl, duration)

class ResourceMusicMetadata(title: String,
                            artist: String,
                            imageUrl: String?,
                            duration: Int,
                            val resourceId: Int): MusicMetadata(title, artist, imageUrl, duration)
