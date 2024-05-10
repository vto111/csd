package byvto.ru.calmsouldev.domain.repository

import byvto.ru.calmsouldev.data.remote.dto.TrackDto

interface CalmSoulRepo {

    suspend fun tracks(): List<TrackDto>

    suspend fun getById(id: String): TrackDto?
}