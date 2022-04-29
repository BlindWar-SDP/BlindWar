package ch.epfl.sdp.blindwar.data.music

import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.data.music.metadata.ResourceMusicMetadata

/**
 * Hard coded constants for demo purposes
 */
object MusicImageUrlConstants {
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

    private val ids = listOf(
        R.raw.lady_gaga_poker_face,
        R.raw.gorillaz_feel_good,
        R.raw.acdc_highway_to_hell,
        R.raw.the_notorious_big_respect,
        R.raw.the_clash_london_calling,
        R.raw.renaud_mistral_gagnant,
        R.raw.red_hot_chili_peppers_californication,
        R.raw.sum_41_in_too_deep,
        R.raw.daft_punk_one_more_time
    )

    val METADATA_TUTORIAL_MUSICS_PER_AUTHOR: Map<String, ResourceMusicMetadata> =
        mutableMapOf<String, ResourceMusicMetadata>().let {
            it["Lady Gaga"] = ResourceMusicMetadata(
                "Poker Face",
                "Lady Gaga",
                LADY_GAGA,
                214000,
                resourceId = ids[0]
            )
            it["Gorillaz"] =
                ResourceMusicMetadata("Feel Good Inc", "Gorillaz", GORILLAZ, 254000, ids[1])
            it["ACDC"] = ResourceMusicMetadata("Highway To Hell", "ACDC", ACDC, 207000, ids[2])
            it["The Notorious BIG"] = ResourceMusicMetadata(
                "Respect",
                "The Notorious BIG",
                THE_NOTORIOUS,
                3200000,
                ids[3]
            )
            it["The Clash"] =
                ResourceMusicMetadata("London Calling", "The Clash", THE_CLASH, 203000, ids[4])
            it["Renaud"] =
                ResourceMusicMetadata("Mistral Gagnant", "Renaud", RENAUD, 162000, ids[5])
            it["Red Hot Chili Peppers"] = ResourceMusicMetadata(
                "Californication",
                "Red Hot Chili Peppers",
                RHCP,
                321000,
                ids[6]
            )
            it["Sum 41"] = ResourceMusicMetadata("In Too Deep", "Sum 41", SUM_41, 222000, ids[7])
            it["Daft Punk"] =
                ResourceMusicMetadata("One More Time", "Daft Punk", DAFT_PUNK, 321000, ids[8])
            it
        }
}