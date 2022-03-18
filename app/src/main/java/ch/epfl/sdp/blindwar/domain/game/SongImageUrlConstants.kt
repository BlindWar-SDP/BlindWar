package ch.epfl.sdp.blindwar.domain.game

/**
 * Hard coded constants for demo purposes
 */
object SongImageUrlConstants {
    const val ACDC: String = "https://i.scdn.co/image/ab67616d00001e0251c02a77d09dfcd53c8676d0"
    const val DAFT_PUNK: String = "https://i.scdn.co/image/ab67616d00001e02b33d46dfa2635a47eebf63b2"
    const val GORILLAZ: String = "https://i.scdn.co/image/ab67616d00001e0219d85a472f328a6ed9b704cf"
    const val LADY_GAGA: String = "https://i.scdn.co/image/ab67616d00001e02e691217483df8798445c82e2"
    const val THE_NOTORIOUS: String = "https://i.scdn.co/image/ab67616d00001e02db09958534ac66f9a90d3cf7"
    const val THE_CLASH: String = "https://i.scdn.co/image/ab67616d00001e026e49cf8fd2505d4dc5368403"
    const val RENAUD: String = "https://i.scdn.co/image/ab67616d00001e02b9cb0f33dc7ae3344f69282a"
    const val RHCP: String = "https://i.scdn.co/image/ab67616d00001e0294d08ab63e57b0cae74e8595"
    const val SUM_41: String = "https://i.scdn.co/image/ab67616d00001e029a482180e6a306229bff49dc"

    val SONG_MAP: Map<String, String> = mutableMapOf<String, String>().let{
        it["Lady Gaga"] = LADY_GAGA
        it["Gorillaz"] = GORILLAZ
        it["ACDC"] = ACDC
        it["The Notorious BIG"] = THE_NOTORIOUS
        it["The Clash"] = THE_CLASH
        it["Renaud"] = RENAUD
        it["Red Hot Chili Peppers"] = RHCP
        it["Sum 41"] = SUM_41
        it["Daft Punk"] = DAFT_PUNK

        it
    }
}