package byvto.ru.calmsoul.data.remote

import byvto.ru.calmsoul.data.remote.dto.TrackDto

interface CalmSoulApi {

    suspend fun tracks(): List<TrackDto>

    suspend fun getById(id: Int): TrackDto
}