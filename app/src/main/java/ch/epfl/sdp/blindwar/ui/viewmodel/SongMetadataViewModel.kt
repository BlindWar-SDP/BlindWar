package ch.epfl.sdp.blindwar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.blindwar.domain.game.SongMetaData

class SongMetadataViewModel : ViewModel() {
    private val mutableMetadata = MutableLiveData<SongMetaData>()
    val selectedMetadata: LiveData<SongMetaData> get() = mutableMetadata

    fun setMeta(meta: SongMetaData) {
        mutableMetadata.value = meta
    }
}