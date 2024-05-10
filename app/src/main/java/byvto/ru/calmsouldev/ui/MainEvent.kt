package byvto.ru.calmsouldev.ui

sealed interface MainEvent {
    data class SmallHeadClick(val id: Int) : MainEvent
    data object BigHeadClick : MainEvent

    //    data class ToggleFinished(val id: Int) : MainEvent
//    data object TogglePlayPause : MainEvent
    data object ResetClick : MainEvent
}
