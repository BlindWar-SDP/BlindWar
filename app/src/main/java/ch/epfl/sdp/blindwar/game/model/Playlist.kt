package ch.epfl.sdp.blindwar.game.model

import ch.epfl.sdp.blindwar.data.music.MusicMetadata
import ch.epfl.sdp.blindwar.game.model.Genre

data class Playlist (
    val name: String, // playlist name
    val author: String, // playlist author
    val genres: List<Genre>, // main genres of the playlist
    val songs: List<MusicMetadata>, // list of playlist's songs metadata
    val imageUrl: String, // playlist cover
    val previewUrl: String, // preview song url
    val difficulty: Difficulty
)

