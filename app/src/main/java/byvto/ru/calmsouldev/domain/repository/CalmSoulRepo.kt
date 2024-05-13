package byvto.ru.calmsouldev.domain.repository

import byvto.ru.calmsouldev.Resource
import byvto.ru.calmsouldev.data.local.TrackEntity
import byvto.ru.calmsouldev.data.remote.dto.TrackDto
import byvto.ru.calmsouldev.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface CalmSoulRepo {

    suspend fun getRemoteList(): Flow<Resource<List<TrackDto>>>
    suspend fun getRemoteById(id: String): Flow<Resource<TrackDto>>
//    suspend fun getRemoteById(id: String): TrackDto?
    suspend fun getLocalList(): List<Track>
    suspend fun getLocalById(id: Int): TrackEntity
    suspend fun addNewTrack(id: Int, description: String, fileName: String, isFinished: Boolean, order: Int)
    suspend fun setFinishedById(id: Int)
    suspend fun resetFinished()
}