package ch.epfl.sdp.blindwar.game.model

/**
 * Represents a displayable element in a recycler view (Playlist, MusicMetadata)
 */
interface Displayable {
    fun getName(): String

    fun getAuthor(): String

    fun getLevel(): String

    fun getGenre(): String

    fun getCover(): String

    fun getPreviewUrl(): String

    fun getSize(): Int

    fun extendable(): Boolean
}