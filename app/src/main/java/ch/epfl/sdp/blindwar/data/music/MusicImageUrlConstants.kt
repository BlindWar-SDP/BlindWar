package ch.epfl.sdp.blindwar.data.music

/**
 * Hard coded constants for demo purposes
 */
object MusicImageUrlConstants {
    const val ACDC: String = "https://i.scdn.co/image/ab67616d00001e0251c02a77d09dfcd53c8676d0"
    const val DAFT_PUNK: String = "https://i.scdn.co/image/ab67616d00001e02b33d46dfa2635a47eebf63b2"
    const val GORILLAZ: String = "https://i.scdn.co/image/ab67616d00001e0219d85a472f328a6ed9b704cf"
    const val LADY_GAGA: String = "https://i.scdn.co/image/ab67616d00001e02e691217483df8798445c82e2"
    const val THE_NOTORIOUS: String =
        "https://i.scdn.co/image/ab67616d00001e02db09958534ac66f9a90d3cf7"
    const val THE_CLASH: String = "https://i.scdn.co/image/ab67616d00001e026e49cf8fd2505d4dc5368403"
    const val RENAUD: String = "https://i.scdn.co/image/ab67616d00001e02b9cb0f33dc7ae3344f69282a"
    const val RHCP: String = "https://i.scdn.co/image/ab67616d00001e0294d08ab63e57b0cae74e8595"
    const val SUM_41: String = "https://i.scdn.co/image/ab67616d00001e029a482180e6a306229bff49dc"

    val METADATA_TUTORIAL_MUSICS_PER_AUTHOR: Map<String, MusicMetadata> = mutableMapOf<String, MusicMetadata>().let{
        it["Lady Gaga"] = MusicMetadata("Poker Face", "Lady Gaga", LADY_GAGA, 214000)
        it["Gorillaz"] = MusicMetadata("Feel Good Inc", "Gorillaz", GORILLAZ, 254000)
        it["ACDC"] = MusicMetadata("Highway To Hell", "ACDC", ACDC, 207000)
        it["The Notorious BIG"] = MusicMetadata("Respect", "The Notorious BIG", THE_NOTORIOUS, 3200000)
        it["The Clash"] = MusicMetadata("London Calling", "The Clash", THE_CLASH, 203000)
        it["Renaud"] = MusicMetadata("Mistral Gagnant", "Renaud", RENAUD, 162000)
        it["Red Hot Chili Peppers"] = MusicMetadata("Californication", "Red Hot Chili Peppers", RHCP, 321000)
        it["Sum 41"] = MusicMetadata("In Too Deep", "Sum 41", SUM_41, 222000)
        it["Daft Punk"] = MusicMetadata("One More Time", "Daft Punk", DAFT_PUNK, 321000)

        it
    }
}