package ch.epfl.sdp.blindwar.data.music.metadata

import kotlinx.serialization.Serializable

@Serializable
class URIMusicMetadata(
    val uri: String = ""
) : MusicMetadata() {
    override fun getPreviewUrl(): String {
        return uri
    }
}
