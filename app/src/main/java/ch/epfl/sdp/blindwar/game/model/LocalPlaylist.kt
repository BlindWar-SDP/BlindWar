package ch.epfl.sdp.blindwar.game.model

import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata

/**
 * Class representing a playlist local
 *
 * @property songs list of playlist's songs metadata
 *
 * @param pid playlist unique id in database
 * @param name
 * @param author
 * @param genres main genres of the playlist
 * @param cover
 * @param previewUrl
 * @param difficulty
 */
class LocalPlaylist(
    pid: String = "",
    name: String = "",
    author: String = "",
    genres: List<Genre> = emptyList(),
    override val songs: ArrayList<MusicMetadata> = ArrayList(),
    cover: String = "",
    previewUrl: String = "",
    difficulty: Difficulty? = null
) : Playlist(pid, name, author, genres, songs, previewUrl, difficulty, cover = cover)