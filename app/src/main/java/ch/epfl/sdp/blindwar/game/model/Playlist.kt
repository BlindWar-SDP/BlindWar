package ch.epfl.sdp.blindwar.game.model

import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata

open class Playlist(
    val uid: String = "", // playlist unique id in database
    override var name: String = "", // playlist name
    override var author: String = "", // playlist author
    val genres: List<Genre> = emptyList(), // main genres of the playlist
    open val songs: List<MusicMetadata> = emptyList(), // list of playlist's songs metadata
    override var previewUrl: String = "", // preview song url
    val difficulty: Difficulty? = null,
    override var genre: String = if (genres.isNotEmpty()) genres.elementAt(0).toString() else "",
    override var cover: String = "",
    override var size: Int = songs.size,
    override val extendable: Boolean = true,
) : Displayable {
    override var level: String
        get() = difficulty.toString()
        set(value) {}
}
