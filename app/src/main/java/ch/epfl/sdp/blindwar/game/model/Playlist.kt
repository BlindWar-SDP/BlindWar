package ch.epfl.sdp.blindwar.game.model

import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata

/**
 * Class representing a playlist
 *
 * @property uid playlist unique id in database
 * @property name
 * @property author
 * @property genres main genres of the playlist
 * @property songs list of playlist's songs metadata
 * @property previewUrl
 * @property difficulty
 * @property genre
 * @property cover
 * @property size
 * @property extendable
 */
open class Playlist(
    val uid: String = "",
    override var name: String = "",
    override var author: String = "",
    val genres: List<Genre> = emptyList(),
    open val songs: List<MusicMetadata> = emptyList(),
    override var previewUrl: String = "",
    val difficulty: Difficulty? = null,
    override var genre: String = if (genres.isNotEmpty()) genres.elementAt(0).toString() else "",
    override var cover: String = "",
    override var size: Int = songs.size,
    override val extendable: Boolean = true,
) : Displayable {
    override var level: String = ""
        get() = difficulty.toString()
}
