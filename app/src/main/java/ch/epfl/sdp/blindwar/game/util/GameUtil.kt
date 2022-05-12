package ch.epfl.sdp.blindwar.game.util

import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.data.music.ReverseMusicConstants
import ch.epfl.sdp.blindwar.data.music.ReverseMusicConstants.METADATA_REVERSE_MUSICS_PER_AUTHOR
import ch.epfl.sdp.blindwar.data.music.metadata.ResourceMusicMetadata
import ch.epfl.sdp.blindwar.data.music.metadata.URIMusicMetadata
import ch.epfl.sdp.blindwar.game.model.Difficulty
import ch.epfl.sdp.blindwar.game.model.Genre
import ch.epfl.sdp.blindwar.game.model.LocalPlaylist
import ch.epfl.sdp.blindwar.game.model.OnlinePlaylist
import ch.epfl.sdp.blindwar.game.model.config.*

object GameUtil {

    const val TIME_TO_FIND = 5000
    const val ROUND = 2

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
        DAFT_PUNK,
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

    fun metadataTutorial(): MutableMap<String, ResourceMusicMetadata> {
        return mutableMapOf<String, ResourceMusicMetadata>().let {
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

    private val PLAYLIST: List<ResourceMusicMetadata> = metadataTutorial().values.toList()
    private val REV_PLAYLIST: List<ResourceMusicMetadata> =
        METADATA_REVERSE_MUSICS_PER_AUTHOR.values.toList()


    private val gameParameter =
        GameParameter(
            round = ROUND,
            funny = false,
            timeToFind = TIME_TO_FIND,
            hint = true,
            lives = 5
        )

    private val gameConfig =
        GameConfig(
            GameMode.REGULAR,
            gameParameter
        )

    const val URL_PREVIEW_FIFA =
        "https://p.scdn.co/mp3-preview/6cc1de8747a673edf568d78a37b03eab86a65c21?cid=774b29d4f13844c495f206cafdad9c86"

    const val URL_FIFA_SONG_2 =
        "https://p.scdn.co/mp3-preview/7c53994cfbd98e4fe15c319ed23862a3bf24ac81?cid=774b29d4f13844c495f206cafdad9c86"

    const val URL_FIFA_SONG_3 =
        "https://p.scdn.co/mp3-preview/9cb161a377591d24bfae1763c9d53c22549bddc5?cid=774b29d4f13844c495f206cafdad9c86"

    private const val URL_PREVIEW_TUTORIAL =
    //    "https://p.scdn.co/mp3-preview/83a6425e6cf360bff80cb93fa3fbc8799cac3894?cid=774b29d4f13844c495f206cafdad9c86"
    //"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/26/70/c9/2670c997-2aa1-550c-477a-fec1708ed552/mzaf_14517577682338403358.plus.aac.p.m4a"
    // "https://p.scdn.co/mp3-preview/33c3aa0656120e384ffdd450a730d62e82869c65?cid=774b29d4f13844c495f206cafdad9c86"
        // "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/86/ff/67/86ff676b-de98-1f74-b90a-b882566ab4a0/mzaf_1515347027412876886.plus.aac.p.m4a"
        "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/ca/6f/88/ca6f884c-ae3a-8503-3f0e-27522dbea1aa/mzaf_3487386545306563563.plus.aac.p.m4a"


    const val COVER_URL_SONG =
        "https://i.scdn.co/image/ab67616d0000b27348eb4d2efa517a920ff4e14e"


    private val searchin = (
            URIMusicMetadata(
                "Searchin",
                "Matisyahu",
                COVER_URL_SONG,
                30000,
                URL_PREVIEW_FIFA
            ))

    private val uatw = URIMusicMetadata(
        "Us Against the World",
        "Clement Marfo",
        "https://i.scdn.co/image/ab67616d0000b273b6e0b1707eea74cd006df458",
        30000,
        URL_FIFA_SONG_2
    )

    val fly = URIMusicMetadata(
        "Fly Or Die",
        "Rock Mafia",
        "https://i.scdn.co/image/ab67616d0000b273711f517eabfb36486a6d96f2",
        30000,
        URL_FIFA_SONG_3
    )

    val fifaPlaylist = OnlinePlaylist(
        "FIFA13",
        "FIFA 13 OST",
        "EA Sports",
        arrayListOf(Genre.POP),
        arrayListOf(
            searchin,
            uatw,
            fly
        ),
        "https://i.scdn.co/image/ab67706c0000bebba1371bd946a7bc3f61f83db4",
        URL_PREVIEW_FIFA,
        Difficulty.EASY
    )

    private val tutorialPlaylist = LocalPlaylist(
        "tutorial",
        "GameUtil",
        "BlindWar",
        arrayListOf(Genre.POP, Genre.RAP),
        PLAYLIST,
        "",
        URL_PREVIEW_TUTORIAL,
        Difficulty.EASY
    )

    private val reversePlaylist = LocalPlaylist(
        "tutorial",
        "Reverse",
        "BlindWar LTD.",
        arrayListOf(Genre.POP, Genre.RAP),
        REV_PLAYLIST,
        "",
        URL_PREVIEW_TUTORIAL,
        Difficulty.EASY
    )

    private const val COVER_TESTING =
        "https://i.scdn.co/image/ab67616d0000b273df756f52b91b4dcd656760b0"

    private const val SONG_TESTING =
        "Silver for Monsters"

    private const val URL_PREVIEW_TESTING =
        "https://p.scdn.co/mp3-preview/ecfd294001cbeea0811b78b35b5a1da80bf3ef98?cid=774b29d4f13844c495f206cafdad9c86"

    private val testingPlaylist = OnlinePlaylist(
        "",
        "The Witcher 3 OST",
        "Marcin Przybyłowicz",
        arrayListOf(Genre.POP),
        arrayListOf(
            URIMusicMetadata(
                SONG_TESTING,
                "Marcin Przybyłowicz",
                COVER_TESTING,
                30000,
                URL_PREVIEW_TESTING
            )
        ),
        COVER_TESTING,
        "https://p.scdn.co/mp3-preview/b2e959350596fc2d3f9ca80e855c51db9a5c5453?cid=774b29d4f13844c495f206cafdad9c86",
        Difficulty.EASY
    )

    val BASE_PLAYLISTS = arrayListOf(tutorialPlaylist, testingPlaylist, reversePlaylist)

    val gameInstanceSolo = GameInstance(gameConfig, tutorialPlaylist, GameFormat.SOLO)
    val gameInstanceMulti = GameInstance(gameConfig, tutorialPlaylist, GameFormat.MULTI)
}