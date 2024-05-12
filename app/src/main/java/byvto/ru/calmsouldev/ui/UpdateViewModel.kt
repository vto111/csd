package byvto.ru.calmsouldev.ui

import android.app.DownloadManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresExtension
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

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val repo: CalmSoulRepo,
    @ApplicationContext context: Context
) : ViewModel() {

    private val _remoteList = MutableStateFlow(listOf<TrackDto>())
    val remoteList = _remoteList.asStateFlow()

    private val _localList = MutableStateFlow(listOf<Track>())
    val localList = _localList.asStateFlow()

    init {
        synchronize(context)
    }

    private fun synchronize(context: Context) {
        viewModelScope.launch {
            _localList.value = repo.getLocalList()
            _remoteList.value = repo.getRemoteList()
            if (_localList.value.isEmpty()) {
                // синхрить все треки (новое устройство)
                _remoteList.value.forEach { track ->
                    addRemoteTrack(context, track.id)
                }
            } else {
                // сравниваем id и синхрим нужные треки
                _remoteList.value.forEach { track ->
                    if ( !_localList.value.map { it.id }.contains(track.id.toInt())) {
//                        Log.i("NEW ID", "downloading track id=${track.id}")
                        addRemoteTrack(context, track.id)
                    }
                }
            }
        }
    }

    private suspend fun addRemoteTrack(context: Context, id: String) {
        val response = repo.getRemoteById(id)
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
            repo.addNewTrack(
                id = response.id.toInt(),
                description = response.description,
                fileName = trackUri.toString(),
                isFinished = false,
                order = response.order
            )
        }
    }
}