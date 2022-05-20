package ch.epfl.sdp.blindwar.data.music.fetcher

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import ch.epfl.sdp.blindwar.audio.ReadyMediaPlayer
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata
import ch.epfl.sdp.blindwar.data.music.metadata.URIMusicMetadata
import java.util.*

class URIFetcher : Fetcher {
    override fun fetchMusic(musicMetadata: MusicMetadata): Pair<MusicMetadata, ReadyMediaPlayer> {

        val player = MediaPlayer()
        val readyMediaPlayer = ReadyMediaPlayer(player, MutableLiveData(true))

        val uriMusicMetadata = musicMetadata as URIMusicMetadata
        val uri = uriMusicMetadata.uri

        player.setDataSource(uri)

        /** TODO: Debug
        player.setOnPreparedListener{
        }

        player.prepareAsync()
         **/
        player.prepare()

        // Random start if the music duration is superior to 30s
        if (uriMusicMetadata.duration > 30000) {
            //TODO: Remove code duplication

            // Keep the start time low enough so that at least half the song can be heard (for now)
            val time = Random().nextInt(uriMusicMetadata.duration.div(2))

            // Change the current music
            player.seekTo(time)
            readyMediaPlayer.ready.postValue(true)
        }

        return Pair(uriMusicMetadata, readyMediaPlayer)
    }
}