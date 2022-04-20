package ch.epfl.sdp.blindwar.data.music

import android.net.Uri

abstract class MusicMetadata(val title: String = "title",
                         val artist: String = "artist",
                         val imageUrl: String? = null,
                         val duration: Int = 0) {
    override fun toString(): String = "$title by $artist"
}

class URIMusicMetadata(title: String,
                       artist: String,
                       imageUrl: String?,
                       duration: Int,
                       val uri: String): MusicMetadata(title, artist, imageUrl, duration)

class ResourceMusicMetadata(title: String,
                            artist: String,
                            imageUrl: String?,
                            duration: Int,
                            val resourceId: Int): MusicMetadata(title, artist, imageUrl, duration)
