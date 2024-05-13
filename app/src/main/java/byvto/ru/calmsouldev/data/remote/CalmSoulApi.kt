package byvto.ru.calmsouldev.data.remote

import byvto.ru.calmsouldev.data.remote.dto.TrackDto

interface CalmSoulApi {

    suspend fun tracks(): List<TrackDto>

    suspend fun getById(id: String): TrackDto
}