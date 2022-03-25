package ch.epfl.sdp.blindwar.domain.game

/**
 * Hard coded constants for demo purposes
 */
object SongImageUrlConstants {
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

    val SONG_MAP: Map<String, SongMetaData> = mutableMapOf<String, SongMetaData>().let{
        it["Lady Gaga"] = SongMetaData("Poker Face", "Lady Gaga", LADY_GAGA)
        it["Gorillaz"] = SongMetaData("Feel Good Inc", "Gorillaz", GORILLAZ)
        it["ACDC"] = SongMetaData("Highway To Hell", "ACDC", ACDC)
        it["The Notorious BIG"] = SongMetaData("Respect", "The Notorious BIG", THE_NOTORIOUS)
        it["The Clash"] = SongMetaData("London Calling", "The Clash", THE_CLASH)
        it["Renaud"] = SongMetaData("Mistral Gagnant", "Renaud", RENAUD)
        it["Red Hot Chili Peppers"] = SongMetaData("Californication", "Red Hot Chili Peppers", RHCP)
        it["Sum 41"] = SongMetaData("In Too Deep", "Sum 41", SUM_41)
        it["Daft Punk"] = SongMetaData("One More Time", "Daft Punk", DAFT_PUNK)
        //it["Daft Punk"] = SongMetaData("Harder Better Faster Stronger", "Daft Punk", DAFT_PUNK)

        it
    }
}