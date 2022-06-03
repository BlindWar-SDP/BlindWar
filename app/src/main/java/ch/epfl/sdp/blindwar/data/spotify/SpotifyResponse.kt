package ch.epfl.sdp.blindwar.data.spotify

/**
 * These classes are used to parse the spotify response
 */

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

data class SpotifySearchTrackResult(
    val href: String,
    val tracks: SpotifySearchTrack,
    val limit: Int,
    val next: String,
    val offset: Int,
    val previous: Any,
    val total: Int
)

data class SpotifySearchTrack(
    val items: List<SpotifyTrack>,
)

data class SpotifyTrack(
    val album: Album,
    val artists: List<ArtistX>,
    val disc_number: Int,
    val duration_ms: Int,
    val explicit: Boolean,
    val external_ids: ExternalIds,
    val external_urls: ExternalUrlsXXX,
    val href: String,
    val id: String,
    val is_local: Boolean,
    val is_playable: Boolean,
    val name: String,
    val popularity: Int,
    val preview_url: String?,
    val track_number: Int,
    val type: String,
    val uri: String
)

data class Album(
    val album_type: String,
    val artists: List<Artist>,
    val external_urls: ExternalUrlsX,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val name: String,
    val release_date: String,
    val release_date_precision: String,
    val total_tracks: Int,
    val type: String,
    val uri: String
)

data class ExternalIds(
    val isrc: String
)

data class ExternalUrls(
    val spotify: String
)

data class ExternalUrlsX(
    val spotify: String
)

data class ExternalUrlsXX(
    val spotify: String
)

data class ExternalUrlsXXX(
    val spotify: String
)

data class Artist(
    val external_urls: ExternalUrls,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)

data class ArtistX(
    val external_urls: ExternalUrlsXX,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)

