package ch.epfl.sdp.blindwar.data.music.metadata
import ch.epfl.sdp.blindwar.game.model.Displayable

abstract class MusicMetadata(var title: String = "",
                         var artist: String = "",
                         var imageUrl: String = "",
                         var duration: Int = 0) : Displayable {
    override fun toString(): String = "$title by $artist"

    override fun getAuthor(): String {
        return artist
    }

    override fun getCover(): String {
        return imageUrl
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



