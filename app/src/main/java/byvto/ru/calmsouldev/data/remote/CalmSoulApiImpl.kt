package byvto.ru.calmsouldev.data.remote

import byvto.ru.calmsouldev.data.remote.dto.TrackDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class CalmSoulApiImpl @Inject constructor(
    private val httpClient: HttpClient
) : CalmSoulApi {
    override suspend fun tracks(): List<TrackDto> {
        return httpClient.get("track").body()
    }

    override suspend fun getById(id: String): TrackDto {
        return httpClient.get("track/$id").body()
    }
}