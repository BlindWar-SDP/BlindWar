package ch.epfl.sdp.blindwar.domain.game

import ch.epfl.sdp.blindwar.domain.game.SongImageUrlConstants.META_DATA_TUTORIAL_MUSICS_PER_AUTHOR
import ch.epfl.sdp.blindwar.ui.solo.Genre
import ch.epfl.sdp.blindwar.ui.solo.PlaylistModel

object Tutorial {

    private val playlist: List<SongMetaData> = META_DATA_TUTORIAL_MUSICS_PER_AUTHOR.values.toList()

    private val gameParameter =
        GameParameter(round = 3, funny = false, timeToFind = 30000, hint = true)

    private val gameConfig =
        GameConfig(
            GameFormat.SOLO,
            GameMode.REGULAR,
            gameParameter
        )

    private const val URL_PREVIEW_FIFA =
        "https://p.scdn.co/mp3-preview/6cc1de8747a673edf568d78a37b03eab86a65c21?cid=774b29d4f13844c495f206cafdad9c86"

    private const val URL_PREVIEW_TUTORIAL =
    //    "https://p.scdn.co/mp3-preview/83a6425e6cf360bff80cb93fa3fbc8799cac3894?cid=774b29d4f13844c495f206cafdad9c86"
        //"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/26/70/c9/2670c997-2aa1-550c-477a-fec1708ed552/mzaf_14517577682338403358.plus.aac.p.m4a"
       // "https://p.scdn.co/mp3-preview/33c3aa0656120e384ffdd450a730d62e82869c65?cid=774b29d4f13844c495f206cafdad9c86"
       // "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/86/ff/67/86ff676b-de98-1f74-b90a-b882566ab4a0/mzaf_1515347027412876886.plus.aac.p.m4a"
        "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/ca/6f/88/ca6f884c-ae3a-8503-3f0e-27522dbea1aa/mzaf_3487386545306563563.plus.aac.p.m4a"
    val fifaPlaylist = PlaylistModel(
        "FIFA 13 OST",
        "EA Sports",
        arrayListOf(Genre.POP),
        arrayListOf(SongMetaData("name", "author", URL_PREVIEW_FIFA)),
        "https://i.scdn.co/image/ab67706c0000bebba1371bd946a7bc3f61f83db4",
        URL_PREVIEW_FIFA
    )

    val tutorialPlaylist = PlaylistModel(
        "Tutorial",
        "BlindWar",
        arrayListOf(Genre.POP),
        playlist,
        "",
        URL_PREVIEW_TUTORIAL
    )

    val gameInstance = GameInstance(gameConfig, playlist)
}