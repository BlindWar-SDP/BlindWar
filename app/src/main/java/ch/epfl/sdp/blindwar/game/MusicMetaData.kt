package ch.epfl.sdp.blindwar.game

data class MusicMetaData(val title: String, val artist: String) {
    override fun toString(): String = "$title by $artist"
}
