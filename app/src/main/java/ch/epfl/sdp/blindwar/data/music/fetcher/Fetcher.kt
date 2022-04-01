package ch.epfl.sdp.blindwar.data.music.fetcher

import android.content.Context
import android.media.MediaPlayer
import ch.epfl.sdp.blindwar.data.music.MusicMetadata

abstract class Fetcher(protected val context: Context)
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
     * MediaPlayer that can play the music once the object is fetched
     */
    lateinit var  mediaPlayer: MediaPlayer
        protected set

    /**
     * Fetch the music and the metadata give an ID
     *
     * @return Return the metadata of the fetched music
     */
    abstract fun fetch()
}