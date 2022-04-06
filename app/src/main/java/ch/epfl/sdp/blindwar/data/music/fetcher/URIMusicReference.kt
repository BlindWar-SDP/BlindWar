package ch.epfl.sdp.blindwar.data.music.fetcher

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

class URIMusicReference(val uri: Uri,
                        context: Context): MusicReference(context) {
     override fun getMediaPlayer(): MediaPlayer {
         val player = MediaPlayer()
         player.setDataSource(uri.toString())
         player.prepareAsync()
         player.setOnPreparedListener{
             isPrepared = true
         }

         return player
     }
 }