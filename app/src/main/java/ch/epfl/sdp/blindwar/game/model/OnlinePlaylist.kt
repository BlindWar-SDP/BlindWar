package ch.epfl.sdp.blindwar.game.model

import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata

/**
 * Class representing a playlist online
 *
 * @property songs list of playlist's songs metadata
 *
 * @param uid playlist unique id in database
 * @param name
 * @param author
 * @param genres main genres of the playlist
 * @param cover
 * @param previewUrl
 * @param difficulty
 */
class OnlinePlaylist(
    uid: String = "",
    name: String = "",
    author: String = "",
    genres: List<Genre> = emptyList(),
    override val songs: List<MusicMetadata> = emptyList(),
    cover: String = "",
    previewUrl: String = "",
    difficulty: Difficulty? = null
) : Playlist(uid, name, author, genres, songs, previewUrl, difficulty, cover = cover)


