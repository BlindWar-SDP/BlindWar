package ch.epfl.sdp.blindwar.data.music.metadata

import ch.epfl.sdp.blindwar.game.model.Displayable

class MusicMetadata(
    var title: String = "",
    var artist: String = "",
    var imageUrl: String = "",
    var duration: Int = 0,
    var uri: String? = null,
    var resourceId: Int? = null
) : Displayable {

    companion object {
        fun createWithURI(title:String, artist:String, imageUrl: String, duration: Int, uri: String): MusicMetadata{
            var musicMetadata = MusicMetadata(title, artist, imageUrl, duration)
            musicMetadata.uri = uri
            return musicMetadata
        }
        fun createWithResourceId(title:String, artist:String, imageUrl: String, duration: Int, resourceId: Int): MusicMetadata{
            var musicMetadata = MusicMetadata(title, artist, imageUrl, duration)
            musicMetadata.resourceId = resourceId
            return musicMetadata
        }
    }

    override fun toString(): String = "$title by $artist"

    override fun getAuthor(): String {
        return artist
    }

    override fun getCover(): String {
        return imageUrl
    }

    override fun getPreviewUrl(): String {
        return ""
    }

    override fun getName(): String {
        return title
    }

    override fun getSize(): Int {
        return 0
    }

    override fun extendable(): Boolean {
        return false
    }
}



