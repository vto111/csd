package byvto.ru.calmsouldev.ui

import android.app.DownloadManager
import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import byvto.ru.calmsouldev.data.local.TrackEntity
import byvto.ru.calmsouldev.data.local.TracksDatabase
import byvto.ru.calmsouldev.data.remote.dto.TrackDto
import byvto.ru.calmsouldev.domain.model.Track
import byvto.ru.calmsouldev.domain.repository.CalmSoulRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val repo: CalmSoulRepo,
    private val db: TracksDatabase,
    @ApplicationContext context: Context
) : ViewModel() {

    private val _remoteList = MutableStateFlow(listOf<TrackDto>())
    val remoteList = _remoteList.asStateFlow()

    private val _localList = MutableStateFlow(listOf<Track>())
    val localList = _localList.asStateFlow()

    init {
        synchronize(context)
    }

    fun onEvent(event: UpdateEvent) {
        when (event) {
            is UpdateEvent.CheckButtonClick -> {
//                getRemoteList()
            }
        }
    }

    private fun synchronize(context: Context) {
        viewModelScope.launch {
            _localList.value = db.dao.getAll().map { it.toTrack() }
            _remoteList.value = repo.tracks()
            if (_localList.value.isEmpty()) {
                //TODO синхрить все треки (новое устройство)
                _remoteList.value.forEach { track ->
//                    println("Add New Track: $track")
                    addRemoteTrack(context, track.id)
                }
            } else {
                //TODO сравниваем id и синхрим нужные треки
                _remoteList.value.forEach { track ->
//                    val l = _localList.value.map { it.id }
                    if (
                    //TODO проверка в локальной базе по id, скачиваем если нет совпадения
                        !_localList.value.map { it.id }.contains(track.id.toInt())
                    ) {
                        Log.i("NEW ID", "downloading track id=${track.id}")
                        addRemoteTrack(context, track.id)
                    }
                }
            }
//            println("RemoteList: ${remoteList.value}")
        }
    }

    private suspend fun addRemoteTrack(context: Context, id: String) {
        val response = repo.getById(id)
        if (response != null) {
            val localFileName = "track${response.id}.ogg"
            val downloadManager = context.getSystemService(DownloadManager::class.java)
            val request =
                DownloadManager.Request("https://api.byvto.ru/uploads/".plus(response.path).toUri())
                    .apply {
                        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                        setDestinationInExternalFilesDir(context, "/tracks", localFileName)
                    }
            downloadManager.enqueue(request)
            val trackUri = File(context.getExternalFilesDir("/tracks"), localFileName).toUri()

            db.dao.insertTrack(
                TrackEntity(
                    id = response.id.toInt(),
                    description = response.description,
                    fileName = trackUri.toString(),
                    isFinished = false,
                    order = response.order
                )
            )
        }
    }

//    private suspend fun downloadRemoteTrack(context: Context, id: String): Uri? {
//        val response = repo.getById(id)
//        if (response != null) {
//            val savename = "track${response.id}.ogg"
//            val downloadManager = context.getSystemService(DownloadManager::class.java)
//            val request =
//                DownloadManager.Request("https://api.byvto.ru/uploads/".plus(response.path).toUri())
//                    .apply {
//                        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
//                        setDestinationInExternalFilesDir(context, "/tracks", savename)
//                    }
//            downloadManager.enqueue(request)
//            val file = File(context.getExternalFilesDir("/tracks"), savename)
//            return file.toUri()
//        }
//        return null
//    }
}