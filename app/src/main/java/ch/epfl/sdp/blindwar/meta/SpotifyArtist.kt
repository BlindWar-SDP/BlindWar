package ch.epfl.sdp.blindwar.meta

data class SpotifyArtist(
    val external_urls: ExternalUrls,
    val followers: Followers,
    val genres: List<String>,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
)

data class Image(
    val height: Int,
    val url: String,
    val width: Int
)

data class Followers(
    val href: Any,
    val total: Int
)

data class ExternalUrls(
    val spotify: String
)