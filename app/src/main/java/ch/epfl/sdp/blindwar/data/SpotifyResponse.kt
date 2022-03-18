package ch.epfl.sdp.blindwar.data

data class SpotifyArtist(
    val external_urls: SpotifyExternalUrls,
    val followers: SpotifyFollowers,
    val genres: List<String>,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
)

data class SpotifyImage(
    val height: Int,
    val url: String,
    val width: Int
)

data class SpotifyFollowers(
    val href: Any,
    val total: Int
)

data class SpotifyExternalUrls(
    val spotify: String
)

data class SpotifyToken(
    var access_token: String,
    val expires_in: Int,
    val token_type: String
)