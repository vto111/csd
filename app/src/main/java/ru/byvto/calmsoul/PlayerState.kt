package ru.byvto.calmsoul

data class PlayerState(
    val id: Int? = null,
    val isPlaying: Boolean = false,
    val fileName: String = "",
    val allDone: Boolean = false,
    val description: String = ""
)
