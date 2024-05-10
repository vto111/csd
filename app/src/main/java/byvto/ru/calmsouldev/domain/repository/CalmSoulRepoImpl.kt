package byvto.ru.calmsouldev.domain.repository

import byvto.ru.calmsouldev.data.remote.CalmSoulApi
import byvto.ru.calmsouldev.data.remote.dto.TrackDto
import javax.inject.Inject

class CalmSoulRepoImpl @Inject constructor(
    private val api: CalmSoulApi
) : CalmSoulRepo {
    override suspend fun tracks(): List<TrackDto> {
        return api.tracks()
    }

    override suspend fun getById(id: String): TrackDto? {
        return api.getById(id = id)
    }
}