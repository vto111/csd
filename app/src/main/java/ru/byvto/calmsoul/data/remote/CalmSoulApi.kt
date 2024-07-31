package ru.byvto.calmsoul.data.remote

import ru.byvto.calmsoul.data.remote.dto.TrackDto

interface CalmSoulApi {

    suspend fun tracks(): List<TrackDto>

    suspend fun getById(id: Int): TrackDto
}