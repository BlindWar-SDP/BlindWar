package ch.epfl.sdp.blindwar.data.music

class URIMusicMetadata(title: String,
                       artist: String,
                       imageUrl: String?,
                       duration: Int,
                       val uri: String): MusicMetadata(title, artist, imageUrl, duration)