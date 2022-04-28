package ch.epfl.sdp.blindwar.data.music

data class ResourceMusicMetadata(
    override var title: String,
    override var artist: String,
    override var imageUrl: String,
    override var duration: Int,
    val resourceId: Int): MusicMetadata()