package ru.byvto.calmsoul.ui

import android.app.DownloadManager
import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.exoplayer.ExoPlayer
import ru.byvto.calmsoul.MainEvent
import ru.byvto.calmsoul.PlayerState
import ru.byvto.calmsoul.domain.model.Track
import ru.byvto.calmsoul.domain.repository.CalmSoulRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.byvto.calmsoul.Resource
import ru.byvto.calmsoul.data.remote.dto.TrackDto
import java.io.File
import java.io.IOException
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: CalmSoulRepo,
    @ApplicationContext context: Context
): ViewModel() {

    private val _playList = MutableStateFlow(listOf<Track>())
    val playList = _playList.asStateFlow()

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.asStateFlow()

    private val _remoteList = MutableStateFlow(listOf<TrackDto>())

    private val _localList = MutableStateFlow(listOf<Track>())

    private val _channel = Channel<MainEvent>()
    val channel = _channel.receiveAsFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex = _currentIndex.asStateFlow()

    private val player = ExoPlayer.Builder(context)
        .build()
        .apply {
            playWhenReady = true
            addListener(
                object : Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_ENDED -> {
                                playerState.value.id?.let { setFinished(it) }
                            }
                            else -> Unit
                        }
                    }
                }
            )
        }

    init {
        viewModelScope.launch {
            synchronize(context)
        }
    }

    fun onEvent(event: MainEvent) {
        when(event) {
            is MainEvent.SmallHeadClick -> repeatFinished(event.id)
            is MainEvent.BigHeadClick -> bigHeadClick()
            is MainEvent.ResetClick -> resetFinished()
            else -> Unit
        }
    }

    private fun showToast(message: String) {
        viewModelScope.launch {
            _channel.send(MainEvent.ShowToast(message))
        }
    }

    private suspend fun synchronize(context: Context) {
        _localList.value = repo.getLocalList()
        repo.getRemoteList()
            .collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _remoteList.value = result.data ?: emptyList()
                        if (_localList.value.isEmpty()) {
                            _remoteList.value.forEach { track ->
                                addRemoteTrack(context, track.id)
                            }
                        } else {
                            _remoteList.value.forEach { track ->
                                if (!_localList.value.map { it.id }
                                        .contains(track.id)) {
                                    addRemoteTrack(context, track.id)
                                }
                            }
//                            showToast("New track downloaded!")
                            _localList.value.forEach { track ->
                                if (!_remoteList.value.map { it.id }
                                        .contains(track.id)) {
                                    deleteTrack(track.id)
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
        _isLoading.value = false
    }

    private suspend fun addRemoteTrack(context: Context, id: Int) {
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
                            //TODO приделать проверку на скачивание файла
                            val trackUri =
                                File(context.getExternalFilesDir("/tracks"), localFileName).toUri()
                            repo.addNewTrack(
                                id = result.data.id,
                                description = result.data.description,
                                fileName = trackUri.toString(),
                                isFinished = false,
                                order = result.data.order
                            )
                            getTrackList()
                        }
                    }

                    is Resource.Error -> {
                        showToast(result.message ?: "addRemoteTrack error!")
                    }

                    is Resource.Loading -> Unit
                }
            }
    }

    private suspend fun deleteTrack(id: Int) {
        val path = URI(repo.getLocalById(id).fileName).path
        try {
            repo.deleteTrackById(id)
            File(path).delete()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getTrackList() {
        viewModelScope.launch {
            _playList.value = repo.getLocalList()
            val finish = playList.value.count {
                !it.isFinished
            }
            if (finish == 0) {
                _playerState.update {
                    it.copy(
                        allDone = true
                    )
                }
            } else {
                _playerState.update {
                    it.copy(
                        allDone = false
                    )
                }
            }
            _isLoading.value = false
        }
    }

    private fun bigHeadClick() {
        if (playerState.value.allDone) {
            return
        }
        if (playerState.value.isPlaying) {
            player.stop()
            _playerState.update {
                it.copy(
                    isPlaying = false,
                    id = null
                )
            }
        } else {
            val rnd = playList.value.filter {
                !it.isFinished
            }.map { it.id }.random()
            viewModelScope.launch {
                playById(rnd)
            }
        }
    }

    private fun repeatFinished(id: Int) {
        viewModelScope.launch {
            playById(id)
        }
        _playerState.update {
            it.copy(
                isPlaying = true
            )
        }
    }

    private suspend fun playById(id: Int) {
        val result = repo.getLocalById(id)
        _playerState.update {
            it.copy(
                id = result.id,
                fileName = result.fileName,
                description = result.description
            )
        }
        val uri = playerState.value.fileName.toUri()
        player.setMediaItem(MediaItem.fromUri(uri))
        player.prepare()
        _playerState.update {
            it.copy(
                isPlaying = true
            )
        }
        // index for scroll to current item
        _currentIndex.value = playList.value.indexOf(playList.value.find { it.id == id })
    }

    private fun setFinished(id: Int) {
        viewModelScope.launch {
            repo.setFinishedById(id)
            _playerState.update {
                it.copy(
                    isPlaying = false
                )
            }
            getTrackList()
        }
    }

    private fun resetFinished() {
        viewModelScope.launch {
            repo.resetFinished()
            _playerState.update {
                it.copy(
                    allDone = false
                )
            }
            getTrackList()
        }
    }
}