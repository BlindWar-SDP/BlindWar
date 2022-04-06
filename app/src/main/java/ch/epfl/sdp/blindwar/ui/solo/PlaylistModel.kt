package ch.epfl.sdp.blindwar.ui.solo

import ch.epfl.sdp.blindwar.data.music.MusicMetadata

data class PlaylistModel (
    val name: String,
    val author: String,
    val genres: List<Genre>,
    val songs: List<MusicMetadata>,
    val imageUrl: String,
    val previewUrl: String
)

enum class Genre {
    POP,
    RAP,
    CLASSIC,
    RNB
}