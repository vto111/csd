package byvto.ru.calmsouldev.domain.model

data class Track(
    val id: Int,
    val fileName: String,
    val isFinished: Boolean,
    val order: Int
)
