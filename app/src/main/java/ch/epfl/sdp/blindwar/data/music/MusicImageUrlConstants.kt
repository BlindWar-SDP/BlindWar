package ch.epfl.sdp.blindwar.data.music

import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.data.music.metadata.ResourceMusicMetadata

/**
 * Hard coded constants for demo purposes
 */
object MusicImageUrlConstants {

    /**
    lateinit var METADATA_TUTORIAL_MUSICS_PER_AUTHOR: Map<String, ResourceMusicMetadata>

    init {
        METADATA_TUTORIAL_MUSICS_PER_AUTHOR =
            mutableMapOf<String, ResourceMusicMetadata>().let {
                for ((index, key) in keys.withIndex()) {
                    it[key.first] = ResourceMusicMetadata(
                        artist = key.first,
                        title = key.second,
                        imageUrl = urls[index],
                        duration = durations[index],
                        resourceId = ids[index]
                    )
                }
                it
            }
    }
    private const val ACDC: String =
        "https://i.scdn.co/image/ab67616d0000b27351c02a77d09dfcd53c8676d0"
    private const val DAFT_PUNK: String =
        "https://i.scdn.co/image/ab67616d0000b273b33d46dfa2635a47eebf63b2"
    private const val GORILLAZ: String =
        "https://i.scdn.co/image/ab67616d0000b27319d85a472f328a6ed9b704cf"
    private const val LADY_GAGA: String =
        "https://i.scdn.co/image/ab67616d0000b273e691217483df8798445c82e2"
    private const val THE_NOTORIOUS: String =
        "https://i.scdn.co/image/ab67616d0000b273db09958534ac66f9a90d3cf7"
    private const val THE_CLASH: String =
        "https://i.scdn.co/image/ab67616d0000b273cd9d8bc9ef04014b6e90e182"
    private const val RENAUD: String =
        "https://i.scdn.co/image/ab67616d0000b273b9cb0f33dc7ae3344f69282a"
    private const val RHCP: String =
        "https://i.scdn.co/image/ab67616d0000b27394d08ab63e57b0cae74e8595"
    private const val SUM_41: String =
        "https://i.scdn.co/image/ab67616d0000b2739a482180e6a306229bff49dc"

    private val urls = listOf(
        LADY_GAGA, // LADY GAGA
       GORILLAZ, // GORILLAZ
        ACDC, // ACDC
        THE_NOTORIOUS, // THE NOTORIOUS
        THE_CLASH,
        RENAUD,
        RHCP,
        SUM_41,
        ReverseMusicConstants.MICHAEL_JACKSON,
        ReverseMusicConstants.QUEEN,
        ReverseMusicConstants.THE_BEATLES,
        ReverseMusicConstants.KLAUS_BADELT,
        ReverseMusicConstants.LED_ZEPPELIN,
        ReverseMusicConstants.DEEP_PURPLE,
        ReverseMusicConstants.THE_CHAMPS
    )

    private val keys = listOf(
        Pair("Lady Gaga", "Poker Face"),
        Pair("Gorillaz", "Feel Good Inc"),
        Pair("ACDC", "Highway to Hell"),
        Pair("The Notorious BIG", "Respect"),
        Pair("The Clash", "London Calling"),
        Pair("Renaud", "Mistral Gagnant"),
        Pair("Red Hot Chili Peppers", "Californication"),
        Pair("Sum 41", "In Too Deep"),
        Pair("Daft Punk", "One More Time"),
        // REVERSED
        Pair("Michael Jackson", "Billie Jean"),
        Pair("Queen", "Bohemian Rhapsody"),
        Pair("The Beatles", "Hey Jude"),
        Pair("Klaus Badelt", "Pirates of the Caribbean"),
        Pair("Deep Purple", "Smoke on the water"),
        Pair("Led Zeppelin", "Stairway to Heaven"),
        Pair("The Champs", "Tequila")
    )

    private val ids = listOf(
        R.raw.lady_gaga_poker_face,
        R.raw.gorillaz_feel_good,
        R.raw.acdc_highway_to_hell,
        R.raw.the_notorious_big_respect,
        R.raw.the_clash_london_calling,
        R.raw.renaud_mistral_gagnant,
        R.raw.red_hot_chili_peppers_californication,
        R.raw.sum_41_in_too_deep,
        R.raw.daft_punk_one_more_time,
        ReverseMusicConstants.idList[0],
        ReverseMusicConstants.idList[1],
        ReverseMusicConstants.idList[2],
        ReverseMusicConstants.idList[3],
        ReverseMusicConstants.idList[4],
        ReverseMusicConstants.idList[5],
        ReverseMusicConstants.idList[6]
    )

    private val durations = listOf(
        214000,
        254000,
        207000,
        3200000,
        203000,
        162000,
        321000,
        222000,
        321000,
        30000,
        30000,
        30000,
        30000,
        30000,
        30000,
        30000
    )
    **/
}