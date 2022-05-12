package ch.epfl.sdp.blindwar.data.music

import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.data.music.metadata.ResourceMusicMetadata

/**
 * Hard coded constants for demo purposes
 */
object ReverseMusicConstants {
    const val MICHAEL_JACKSON: String =
        "https://i.scdn.co/image/ab6761610000e5eba2a0b9e3448c1e702de9dc90"
    const val QUEEN: String =
        "https://i.scdn.co/image/ab67616d0000b273dada67d578bea0b446036e87"
    const val THE_BEATLES: String =
        "https://i.scdn.co/image/ab6761610000e5ebe9348cc01ff5d55971b22433"
    const val KLAUS_BADELT: String =
        "https://i.scdn.co/image/ab67616d0000b27338786c7492ac252797bb2648"
    const val DEEP_PURPLE: String =
        "https://i.scdn.co/image/ab6761610000e5eb23a7b4c49d285729a974d6dd"
    const val LED_ZEPPELIN: String =
        "https://i.scdn.co/image/207803ce008388d3427a685254f9de6a8f61dc2e"
    const val THE_CHAMPS: String =
        "https://i.scdn.co/image/ab67616d00001e02621b1b8858b7bfe2706fb7bb"


    val idList = listOf(
        R.raw.billy_reversed,
        R.raw.bohemian_reversed,
        R.raw.hey_jude_reversed,
        R.raw.pirates_reversed,
        R.raw.smoke_reversed,
        R.raw.stairway_reversed,
        R.raw.tequila_reversed
    )

    val METADATA_REVERSE_MUSICS_PER_AUTHOR: Map<String, ResourceMusicMetadata> =
        mutableMapOf<String, ResourceMusicMetadata>().let {
            it["Michael Jackson"] = ResourceMusicMetadata(
                "Billie Jean", "Michael Jackson",
                MICHAEL_JACKSON, 30000, resourceId = idList[0]
            )
            it["Queen"] = ResourceMusicMetadata(
                "Bohemian Rhapsody", "Queen",
                QUEEN, 30000, idList[1]
            )
            it["The Beatles"] = ResourceMusicMetadata(
                "Hey Jude", "The Beatles",
                THE_BEATLES, 30000, idList[2]
            )
            it["Klaus Badelt"] = ResourceMusicMetadata(
                "Pirates of the Caribbean", "Klaus Badelt",
                KLAUS_BADELT, 30000, idList[3]
            )
            it["Deep Purple"] = ResourceMusicMetadata(
                "Smoke on the water", "Deep Purple",
                DEEP_PURPLE, 30000, idList[4]
            )
            it["Led Zeppelin"] = ResourceMusicMetadata(
                "Stairway to Heaven", "Led Zeppelin",
                LED_ZEPPELIN, 30000, idList[5]
            )
            it["The Champs"] = ResourceMusicMetadata(
                "Tequila", "The Champs",
                THE_CHAMPS, 30000, idList[6]
            )
            it
        }
}