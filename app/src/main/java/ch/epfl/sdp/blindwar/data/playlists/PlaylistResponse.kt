package ch.epfl.sdp.blindwar.data.playlists

import ch.epfl.sdp.blindwar.game.model.OnlinePlaylist

data class PlaylistResponse(
    var onlinePlaylists: List<OnlinePlaylist>? = null,
)