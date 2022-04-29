package ch.epfl.sdp.blindwar.data.music


abstract class MusicMetadata {
    abstract var title: String
    abstract var artist: String
    abstract var imageUrl: String
    abstract var duration: Int
    //override fun toString(): String = "$title by $artist"
}



