package ch.epfl.sdp.blindwar.data.playlists

import ch.epfl.sdp.blindwar.game.model.Playlist

data class PlaylistResponse(
    var playlists: List<Playlist>? = null,
    var exception: Exception? = null
)