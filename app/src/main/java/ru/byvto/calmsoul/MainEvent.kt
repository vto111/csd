package ru.byvto.calmsoul

sealed interface MainEvent {
    data class SmallHeadClick(val id: Int) : MainEvent
    data object BigHeadClick : MainEvent
    data object ResetClick : MainEvent
    data class ShowToast(val message: String): MainEvent
}
