package ch.epfl.sdp.blindwar.data.music.fetcher

import android.content.Context
import android.media.MediaPlayer
import ch.epfl.sdp.blindwar.data.music.MusicMetadata

abstract class MusicReference(protected val context: Context)
{
    /**
     * Is the music already fetched ?
     */
    var isFetched: Boolean = false
        protected set

    /**
     * Metadata of the fetched music. Only available when the data are fetched
     */
    lateinit var musicMetadata: MusicMetadata
        protected set

    /**
     * Fetch the media player
     *
     * @return Media player of the fetched music
     */
    abstract fun getMediaPlayer(): MediaPlayer
}