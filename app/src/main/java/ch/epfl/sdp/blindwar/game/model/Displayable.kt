package ch.epfl.sdp.blindwar.game.model

/**
 * Represents a displayable element in a recycler view (Playlist, MusicMetadata)
 */
interface Displayable {
    var name: String
    var author: String
    var level: String
    var genre: String
    var cover: String
    var previewUrl: String
    var size: Int
    val extendable: Boolean
}