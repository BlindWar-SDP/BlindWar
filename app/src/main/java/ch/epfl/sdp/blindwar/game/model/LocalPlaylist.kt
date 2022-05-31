package ch.epfl.sdp.blindwar.game.model

import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata


class LocalPlaylist(
    pid: String, // playlist unique id in database
    name: String, // playlist name
    author: String = "", // playlist author
    genres: List<Genre> = emptyList(), // main genres of the playlist
    override val songs: List<MusicMetadata> = emptyList(), // list of playlist's songs metadata
    imageUrl: String = "", // playlist cover
    previewUrl: String = "", // preview song url
    difficulty: Difficulty? = null
) : Playlist(pid, name, author, genres, songs, previewUrl, difficulty, cover = imageUrl)