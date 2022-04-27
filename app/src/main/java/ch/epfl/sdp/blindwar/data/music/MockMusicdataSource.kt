package ch.epfl.sdp.blindwar.data.music

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ch.epfl.sdp.blindwar.game.util.Tutorial

object MockMusicdataSource {

    val searchin = (
            URIMusicMetadata("Searchin",
                "Matisyahu",
                Tutorial.COVER_URL_SONG,
                30000,
                Tutorial.URL_PREVIEW_FIFA
            ))

    val uatw = URIMusicMetadata(
        "Us Against the World",
        "Clement Marfo",
        "https://i.scdn.co/image/ab67616d0000b273b6e0b1707eea74cd006df458",
        30000,
        Tutorial.URL_FIFA_SONG_2
    )

    val fly = URIMusicMetadata(
        "Fly Or Die",
        "Rock Mafia",
        "https://i.scdn.co/image/ab67616d0000b273711f517eabfb36486a6d96f2",
        30000,
        Tutorial.URL_FIFA_SONG_3
    )

    fun fetchMusicMetadata(): ArrayList<MusicMetadata> {
        return arrayListOf(searchin, uatw, fly)
    }
}