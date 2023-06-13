package byvto.ru.calmsouldev.data

sealed interface SoundTrackEvent{
    object SaveSoundTrack: SoundTrackEvent
    data class SetAllSoundTrack(val soundTrack: List<String>): SoundTrackEvent
}