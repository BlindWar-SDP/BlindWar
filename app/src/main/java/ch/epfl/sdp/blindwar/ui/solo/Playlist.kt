package ch.epfl.sdp.blindwar.ui.solo

import ch.epfl.sdp.blindwar.domain.game.SongMetaData

data class Playlist (
    val name: String,
    val songs: List<SongMetaData>,
    val imageUrl: String
)
