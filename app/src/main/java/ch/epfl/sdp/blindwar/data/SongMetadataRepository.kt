package ch.epfl.sdp.blindwar.data

import ch.epfl.sdp.blindwar.ui.SongMetaData
import kotlinx.coroutines.flow.Flow

class SongMetadataRepository (private val remoteMetadataSource: SpotifyApi)
