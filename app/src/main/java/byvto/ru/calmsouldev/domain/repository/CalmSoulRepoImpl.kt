package byvto.ru.calmsouldev.domain.repository

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import byvto.ru.calmsouldev.Resource
import byvto.ru.calmsouldev.data.local.TrackEntity
import byvto.ru.calmsouldev.data.local.TracksDao
import byvto.ru.calmsouldev.data.local.TracksDatabase
import byvto.ru.calmsouldev.data.remote.CalmSoulApi
import byvto.ru.calmsouldev.data.remote.dto.TrackDto
import byvto.ru.calmsouldev.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import java.net.ConnectException
import javax.inject.Inject

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class CalmSoulRepoImpl @Inject constructor(
    private val api: CalmSoulApi,
    private val db: TracksDatabase,
) : CalmSoulRepo {

    override suspend fun getRemoteList(): Flow<Resource<List<TrackDto>>> = flow {
        emit(Resource.Loading())
        try {
            val result = api.tracks()
            emit(Resource.Success(result))
        } catch (e: HttpException) {
//            Log.e("HttpException", "Connection error")
            emit(Resource.Error(
                message = "Http Connection Error!",
                data = emptyList()
            ))
            e.printStackTrace()
        }
        catch (e: IOException) {
//            Log.e("IOException", "Connection error")
            emit(Resource.Error(
                message = "Network Connection Error!",
                data = emptyList()
            ))
            e.printStackTrace()
        }
    }

    override suspend fun getRemoteById(id: String): Flow<Resource<TrackDto>> = flow {
        emit(Resource.Loading())
        try {
            val result = api.getById(id = id)
            emit(Resource.Success(result))
        } catch (e: HttpException) {
            Log.e("HttpException", "Connection error")
            emit(Resource.Error(
                message = "Http Connection Error!",
                data = null
            ))
            e.printStackTrace()
        }
        catch (e: IOException) {
            Log.e("IOException", "Connection error")
            emit(Resource.Error(
                message = "Network Connection Error!",
                data =  null
            ))
            e.printStackTrace()
        }
    }

    override suspend fun getLocalList(): List<Track> {
        return db.dao.getAll().map { it.toTrack() }
    }

    override suspend fun getLocalById(id: Int): TrackEntity {
        return db.dao.getById(id)
    }

    override suspend fun addNewTrack(
        id: Int,
        description: String,
        fileName: String,
        isFinished: Boolean,
        order: Int
    ) {
        db.dao.insertTrack(
            TrackEntity(
                id = id,
                description = description,
                fileName = fileName,
                isFinished = false,
                order = order
            )
        )
    }

    override suspend fun setFinishedById(id: Int) {
        db.dao.setFinished(id)
    }

    override suspend fun resetFinished() {
        db.dao.resetFinished()
    }
}