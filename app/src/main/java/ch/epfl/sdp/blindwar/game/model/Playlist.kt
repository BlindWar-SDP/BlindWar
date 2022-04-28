package ch.epfl.sdp.blindwar.game.model

import ch.epfl.sdp.blindwar.data.music.MusicMetadata

open class Playlist(
    val uid: String = "", // playlist unique id in database
    val name: String = "", // playlist name
    val author: String = "", // playlist author
    val genres: List<Genre> = emptyList(), // main genres of the playlist
    open val songs: List<MusicMetadata> = emptyList(), // list of playlist's songs metadata
    val imageUrl: String = "", // playlist cover
    val previewUrl: String = "", // preview song url
    val difficulty: Difficulty = Difficulty.EASY
)