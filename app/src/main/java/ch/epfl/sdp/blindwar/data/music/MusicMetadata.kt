package ch.epfl.sdp.blindwar.data.music

data class MusicMetadata(val title: String, val artist: String, val imageUrl: String? = null, val duration: Int) {
    override fun toString(): String = "$title by $artist"
}
