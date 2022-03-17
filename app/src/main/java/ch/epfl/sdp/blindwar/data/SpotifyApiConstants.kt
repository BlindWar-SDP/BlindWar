package ch.epfl.sdp.blindwar.data

object SpotifyApiConstants {
    /** Credentials constants **/
    const val CLIENT_ID = "66f4e3c14d6e425caed973ba62bb2077";
    const val CLIENT_SECRET = "c0d9389a443646ba8b47496d84862075";
    const val AUTH_TYPE = "client_credentials"

    /** Headers constants **/
    const val AUTH = "Authorization"
    const val ACCEPT = "Accept: application/json"
    const val CONTENT_TYPE = "Content-Type: application/json"
    const val URL_ENCODED_FORM = "Content-Type: application/x-www-form-urlencoded"

    /** URL path constants **/
    const val API_PATH = "api/token/"
    const val ARTIST_PATH = "artists/{artist_id}"
    const val TRACK_PATH = "tracks/{track_id}"

    /** Query and field constants **/
    const val ARTIST_ID = "artist_id"
    const val TRACK_ID = "track_id"
    const val GRANT_TYPE = "grant_type"
    const val LIMIT = "limit"
}