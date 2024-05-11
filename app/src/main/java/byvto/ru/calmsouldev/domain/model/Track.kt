package byvto.ru.calmsouldev.domain.model

data class Track(
    val id: Int,
    val description: String,
    val fileName: String,
    val isFinished: Boolean,
    val order: Int
)
