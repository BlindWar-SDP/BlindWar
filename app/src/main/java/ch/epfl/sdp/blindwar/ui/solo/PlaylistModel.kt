package ch.epfl.sdp.blindwar.ui.solo

import ch.epfl.sdp.blindwar.domain.game.SongMetaData

data class PlaylistModel (
    val name: String,
    val author: String,
    val genres: List<Genre>,
    val songs: List<SongMetaData>,
    val imageUrl: String,
    val previewUrl: String
)

enum class Genre {
    POP,
    RAP,
    CLASSIC,
    RNB
}
