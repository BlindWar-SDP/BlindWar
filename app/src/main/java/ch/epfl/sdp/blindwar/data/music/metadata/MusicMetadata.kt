package ch.epfl.sdp.blindwar.data.music.metadata

import ch.epfl.sdp.blindwar.game.model.Displayable
import java.io.Serializable

open class MusicMetadata(
    var duration: Int = 0,
    var uri: String? = null,
    var resourceId: Int? = null,
    override var name: String = "",
    override var author: String = "",
    override var level: String = "",
    override var genre: String = "",
    override var cover: String = "",
    override var previewUrl: String = "",
    override var size: Int = 0,
    override val extendable: Boolean = false,
) : Displayable, Serializable {
    companion object {
        /**
         * Create e musicMetadata from uri
         *
         * @param name
         * @param author
         * @param cover
         * @param duration
         * @param uri
         * @return
         */
        fun createWithURI(
            name: String,
            author: String,
            cover: String,
            duration: Int,
            uri: String
        ): MusicMetadata {
            val musicMetadata =
                MusicMetadata(name = name, author = author, cover = cover, duration = duration)
            musicMetadata.uri = uri
            return musicMetadata
        }

        /**
         * Create a musicMetadata object from resources
         *
         * @param name
         * @param author
         * @param cover
         * @param duration
         * @param resourceId
         * @return
         */
        fun createWithResourceId(
            name: String,
            author: String,
            cover: String,
            duration: Int,
            resourceId: Int
        ): MusicMetadata {
            val musicMetadata =
                MusicMetadata(name = name, author = author, cover = cover, duration = duration)
            musicMetadata.resourceId = resourceId
            return musicMetadata
        }
    }

    /**
     * @return "$name by $author"
     */
    override fun toString(): String = "$name by $author"
}



