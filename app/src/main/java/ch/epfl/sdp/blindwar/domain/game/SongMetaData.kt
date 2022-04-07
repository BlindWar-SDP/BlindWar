package ch.epfl.sdp.blindwar.domain.game

data class SongMetaData(val title: String, val artist: String, val imageUrl: String? = null) {
    override fun toString(): String = "$title by $artist"
}