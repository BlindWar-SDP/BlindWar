package ch.epfl.sdp.blindwar.data.music

abstract class MusicMetadata(var title: String = "",
                         var artist: String = "",
                         var imageUrl: String = "",
                         var duration: Int = 0) {
    override fun toString(): String = "$title by $artist"
}



