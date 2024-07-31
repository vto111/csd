package ru.byvto.calmsoul.domain.repository

import ru.byvto.calmsoul.Resource
import ru.byvto.calmsoul.data.local.TrackEntity
import ru.byvto.calmsoul.data.remote.dto.TrackDto
import ru.byvto.calmsoul.domain.model.Track
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