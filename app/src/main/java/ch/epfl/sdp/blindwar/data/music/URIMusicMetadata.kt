package ch.epfl.sdp.blindwar.data.music

import kotlinx.serialization.Serializable

@Serializable
data class URIMusicMetadata(
    override var title: String = "",
    override var artist: String = "",
    override var imageUrl: String = "",
    override var duration: Int = 0,
    val uri: String = ""): MusicMetadata()