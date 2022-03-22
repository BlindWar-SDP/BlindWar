package ch.epfl.sdp.blindwar.domain.game

data class SongInstance(
    val metaData: SongMetaData,
    val time: Int,
    val liked: Boolean,
    val success: Boolean
)
