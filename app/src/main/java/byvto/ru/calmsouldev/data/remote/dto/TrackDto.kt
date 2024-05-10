package byvto.ru.calmsouldev.data.remote.dto

import byvto.ru.calmsouldev.domain.model.Track

data class TrackDto(
    val id: String,
    val description: String,
    val path: String,
    val order: Int
)
