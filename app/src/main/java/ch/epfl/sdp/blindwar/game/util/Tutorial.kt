package ch.epfl.sdp.blindwar.game.util

import ch.epfl.sdp.blindwar.data.music.MusicImageUrlConstants.METADATA_TUTORIAL_MUSICS_PER_AUTHOR
import ch.epfl.sdp.blindwar.data.music.ResourceMusicMetadata
import ch.epfl.sdp.blindwar.data.music.ReverseMusicConstants.METADATA_REVERSE_MUSICS_PER_AUTHOR
import ch.epfl.sdp.blindwar.data.music.URIMusicMetadata
import ch.epfl.sdp.blindwar.game.model.*
import ch.epfl.sdp.blindwar.game.model.Genre
import ch.epfl.sdp.blindwar.game.model.OnlinePlaylist
import ch.epfl.sdp.blindwar.game.model.config.*

object Tutorial {

    const val TIME_TO_FIND = 5000
    const val ROUND = 2

    private val PLAYLIST: List<ResourceMusicMetadata> = METADATA_TUTORIAL_MUSICS_PER_AUTHOR.values.toList()
    private val REV_PLAYLIST: List<ResourceMusicMetadata> = METADATA_REVERSE_MUSICS_PER_AUTHOR.values.toList()

    private val gameParameter =
        GameParameter(round = ROUND, funny = false, timeToFind = TIME_TO_FIND, hint = true, lives = 5)

    private val gameConfig =
        GameConfig (
            GameFormat.SOLO,
            GameMode.REGULAR,
            gameParameter
        )

    private const val URL_PREVIEW_FIFA =
        "https://p.scdn.co/mp3-preview/6cc1de8747a673edf568d78a37b03eab86a65c21?cid=774b29d4f13844c495f206cafdad9c86"

    private const val URL_FIFA_SONG_2 =
        "https://p.scdn.co/mp3-preview/7c53994cfbd98e4fe15c319ed23862a3bf24ac81?cid=774b29d4f13844c495f206cafdad9c86"

    private const val URL_FIFA_SONG_3 =
        "https://p.scdn.co/mp3-preview/9cb161a377591d24bfae1763c9d53c22549bddc5?cid=774b29d4f13844c495f206cafdad9c86"

    private const val URL_PREVIEW_TUTORIAL =
    //    "https://p.scdn.co/mp3-preview/83a6425e6cf360bff80cb93fa3fbc8799cac3894?cid=774b29d4f13844c495f206cafdad9c86"
        //"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/26/70/c9/2670c997-2aa1-550c-477a-fec1708ed552/mzaf_14517577682338403358.plus.aac.p.m4a"
       // "https://p.scdn.co/mp3-preview/33c3aa0656120e384ffdd450a730d62e82869c65?cid=774b29d4f13844c495f206cafdad9c86"
       // "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/86/ff/67/86ff676b-de98-1f74-b90a-b882566ab4a0/mzaf_1515347027412876886.plus.aac.p.m4a"
        "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/ca/6f/88/ca6f884c-ae3a-8503-3f0e-27522dbea1aa/mzaf_3487386545306563563.plus.aac.p.m4a"


    private const val COVER_URL_SONG =
        "https://i.scdn.co/image/ab67616d0000b27348eb4d2efa517a920ff4e14e"



    val fifaPlaylist = OnlinePlaylist("FIFA13",
        "FIFA 13 OST",
        "EA Sports",
        arrayListOf(Genre.POP),
        arrayListOf(
            URIMusicMetadata("Searchin",
            "Matisyahu",
            COVER_URL_SONG,
            30000,
            URL_PREVIEW_FIFA
            ),
            URIMusicMetadata(
                "Us Against the World",
                "Clement Marfo",
                "https://i.scdn.co/image/ab67616d0000b273b6e0b1707eea74cd006df458",
                30000,
                URL_FIFA_SONG_2
            ),
            URIMusicMetadata(
                "Fly Or Die",
                "Rock Mafia",
                "https://i.scdn.co/image/ab67616d0000b273711f517eabfb36486a6d96f2",
                30000,
                URL_FIFA_SONG_3
            )
        ),
        "https://i.scdn.co/image/ab67706c0000bebba1371bd946a7bc3f61f83db4",
        URL_PREVIEW_FIFA,
        Difficulty.EASY
    )

    private val tutorialPlaylist = LocalPlaylist("tutorial",
        "Tutorial",
        "BlindWar",
        arrayListOf(Genre.POP, Genre.RAP),
        PLAYLIST,
        "",
        URL_PREVIEW_TUTORIAL,
        Difficulty.EASY
    )

    private val reversePlaylist = LocalPlaylist("tutorial",
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

    const val SONG_TESTING =
        "Silver for Monsters"

    const val URL_PREVIEW_TESTING =
        "https://p.scdn.co/mp3-preview/ecfd294001cbeea0811b78b35b5a1da80bf3ef98?cid=774b29d4f13844c495f206cafdad9c86"

    private val testingPlaylist = OnlinePlaylist("",
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
            )),
        COVER_TESTING,
        "https://p.scdn.co/mp3-preview/b2e959350596fc2d3f9ca80e855c51db9a5c5453?cid=774b29d4f13844c495f206cafdad9c86",
        Difficulty.EASY
    )

    val BASE_PLAYLISTS = arrayListOf(tutorialPlaylist, testingPlaylist, reversePlaylist)

    val gameInstance = GameInstance(gameConfig, tutorialPlaylist)
}