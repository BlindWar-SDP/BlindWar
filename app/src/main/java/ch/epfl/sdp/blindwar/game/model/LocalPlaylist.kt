package ch.epfl.sdp.blindwar.game.model

import ch.epfl.sdp.blindwar.data.music.ResourceMusicMetadata

class LocalPlaylist(
    uid: String, // playlist unique id in database
    name: String, // playlist name
    author: String = "", // playlist author
    genres: List<Genre> = emptyList(), // main genres of the playlist
    override val songs: List<ResourceMusicMetadata> = emptyList(), // list of playlist's songs metadata
    imageUrl: String = "", // playlist cover
    previewUrl: String = "", // preview song url
    difficulty: Difficulty = Difficulty.EASY
) : Playlist(uid, name, author, genres, songs, imageUrl, previewUrl, difficulty)