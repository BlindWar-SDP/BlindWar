package ch.epfl.sdp.blindwar.data.music.metadata

class ResourceMusicMetadata(title: String,
                            artist: String,
                            imageUrl: String,
                            duration: Int,
                            val resourceId: Int): MusicMetadata(title, artist, imageUrl, duration) {
    override fun getPreviewUrl(): String {
        return ""
    }
}
