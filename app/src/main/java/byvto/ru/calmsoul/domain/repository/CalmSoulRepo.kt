package byvto.ru.calmsoul.domain.repository

import byvto.ru.calmsoul.Resource
import byvto.ru.calmsoul.data.local.TrackEntity
import byvto.ru.calmsoul.data.remote.dto.TrackDto
import byvto.ru.calmsoul.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface CalmSoulRepo {

    suspend fun getRemoteList(): Flow<Resource<List<TrackDto>>>
    suspend fun getRemoteById(id: Int): Flow<Resource<TrackDto>>
    suspend fun getLocalList(): List<Track>
    suspend fun getLocalById(id: Int): TrackEntity
    suspend fun addNewTrack(
        id: Int,
        description: String,
        fileName: String,
        isFinished: Boolean,
        order: Int
    )
    suspend fun setFinishedById(id: Int)
    suspend fun resetFinished()
    suspend fun deleteTrackById(id: Int)
}