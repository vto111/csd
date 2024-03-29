package byvto.ru.calmsouldev

sealed interface MainEvent {
    data class SmallHeadClick(val id: Int) : MainEvent
    object BigHeadClick : MainEvent
    data class ToggleFinished(val id: Int) : MainEvent
    object TogglePlayPause : MainEvent
}
