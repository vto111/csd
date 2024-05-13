package byvto.ru.calmsouldev.ui

import android.app.DownloadManager
import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import byvto.ru.calmsouldev.Resource
import byvto.ru.calmsouldev.data.remote.dto.TrackDto
import byvto.ru.calmsouldev.domain.model.Track
import byvto.ru.calmsouldev.domain.repository.CalmSoulRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val repo: CalmSoulRepo,
    @ApplicationContext context: Context
) : ViewModel() {

    private val _remoteList = MutableStateFlow(listOf<TrackDto>())
    val remoteList = _remoteList.asStateFlow()

    private val _localList = MutableStateFlow(listOf<Track>())
    val localList = _localList.asStateFlow()

    private val _channel = Channel<CalmSoulEvent>()
    val channel = _channel.receiveAsFlow()

    init {
//        synchronize(context)
        updateLists(context)
    }

    private fun showToast(message: String) {
        viewModelScope.launch {
            _channel.send(CalmSoulEvent.ShowToast(message))
        }
    }

    private fun updateLists(context: Context) {
        viewModelScope.launch {
            _localList.value = repo.getLocalList()
            repo.getRemoteList()
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _remoteList.value = result.data ?: emptyList()
                        }

                        is Resource.Error -> {
                            _remoteList.value = result.data ?: emptyList()
                            showToast(result.message ?: "Unknown error!")
                        }

                        is Resource.Loading -> {
                            _remoteList.value = result.data ?: emptyList()
                        }
                    }
                }
        }
    }

    private fun synchronize(context: Context) {
        viewModelScope.launch {
            _localList.value = repo.getLocalList()
            repo.getRemoteList()
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _remoteList.value = result.data ?: emptyList()
                            if (_localList.value.isEmpty()) {
                                // синхрить все треки (новое устройство)
                                _remoteList.value.forEach { track ->
                                    addRemoteTrack(context, track.id)
                                }
                            } else {
                                // сравниваем id и синхрим нужные треки
                                _remoteList.value.forEach { track ->
                                    if (!_localList.value.map { it.id }
                                            .contains(track.id.toInt())) {
                                        addRemoteTrack(context, track.id)
                                    }
                                }
                            }
                        }

                        is Resource.Error -> {
                            _remoteList.value = result.data ?: emptyList()
                            showToast(result.message ?: "Unknown error!")
                        }

                        is Resource.Loading -> {
                            _remoteList.value = result.data ?: emptyList()
                        }
                    }
                }
            _localList.value = repo.getLocalList()
        }
    }

    private suspend fun addRemoteTrack(context: Context, id: String) {
        repo.getRemoteById(id)
            .collect { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data != null) {
                            val localFileName = "track${result.data.id}.ogg"
                            val downloadManager =
                                context.getSystemService(DownloadManager::class.java)
                            val request =
                                DownloadManager.Request(
                                    "https://api.byvto.ru/uploads/".plus(result.data.path).toUri()
                                )
                                    .apply {
                                        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                                        setDestinationInExternalFilesDir(
                                            context,
                                            "/tracks",
                                            localFileName
                                        )
                                    }
                            downloadManager.enqueue(request)
                            val trackUri =
                                File(context.getExternalFilesDir("/tracks"), localFileName).toUri()
                            repo.addNewTrack(
                                id = result.data.id.toInt(),
                                description = result.data.description,
                                fileName = trackUri.toString(),
                                isFinished = false,
                                order = result.data.order
                            )
                        }
                    }

                    is Resource.Error -> {
                        showToast(result.message ?: "Unknown error!")
                    }

                    is Resource.Loading -> {}
                }
            }
    }
}