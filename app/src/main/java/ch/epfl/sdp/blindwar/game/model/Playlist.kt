package ch.epfl.sdp.blindwar.game.model

import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata

open class Playlist(
    val uid: String = "", // playlist unique id in database
    private val name: String = "", // playlist name
    private val author: String = "", // playlist author
    val genres: List<Genre> = emptyList(), // main genres of the playlist
    open val songs: List<MusicMetadata> = emptyList(), // list of playlist's songs metadata
    val imageUrl: String = "", // playlist cover
    private val previewUrl: String = "", // preview song url
    val difficulty: Difficulty = Difficulty.EASY
) : Displayable {
    override fun getName(): String {
        return name
    }

    override fun getAuthor(): String {
        return author
    }

    override fun getCover(): String {
        return imageUrl
    }

    override fun getPreviewUrl(): String {
        return previewUrl
    }

    override fun getSize(): Int {
        return songs.size
    }

    override fun extendable(): Boolean {
        return true
    }
}
