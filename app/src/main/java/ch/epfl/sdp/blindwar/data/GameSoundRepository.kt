package ch.epfl.sdp.blindwar.data

import ch.epfl.sdp.blindwar.data.sound.LocalSoundDataSource
import ch.epfl.sdp.blindwar.data.sound.RemoteSoundDataSource

class GameSoundRepository(val localSource: LocalSoundDataSource,
val remoteSource: RemoteSoundDataSource) {

}