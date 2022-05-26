package ch.epfl.sdp.blindwar.data.music.metadata

class URIMusicMetadata(
    title: String = "",
    artist: String = "",
    imageUrl: String = "",
    duration: Int = 0,
) : MusicMetadata(title, artist, imageUrl, duration) {
    override fun getPreviewUrl(): String {
        return uri!!
    }
}
