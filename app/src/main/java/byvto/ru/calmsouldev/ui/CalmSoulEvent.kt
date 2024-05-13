package byvto.ru.calmsouldev.ui

sealed class CalmSoulEvent {
    data class ShowToast(val message: String): CalmSoulEvent()

}