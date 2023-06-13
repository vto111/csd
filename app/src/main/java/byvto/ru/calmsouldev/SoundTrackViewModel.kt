package byvto.ru.calmsouldev

import androidx.lifecycle.ViewModel
import byvto.ru.calmsouldev.data.SoundTrackDao
import byvto.ru.calmsouldev.data.SoundTrackState
import kotlinx.coroutines.flow.MutableStateFlow

class SoundTrackViewModel(
    private val dao: SoundTrackDao
): ViewModel() {
    private val _state = MutableStateFlow(SoundTrackState())
}