package ru.byvto.calmsoul.ui

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    fun onEvent(event: MainEvent) {
        when(event) {
            is MainEvent.SmallHeadClick -> repeatFinished(event.id)
            is MainEvent.BigHeadClick -> bigHeadClick()
            is MainEvent.ResetClick -> resetFinished()
            else -> Unit
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
            }
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
//            _playerState.update {
//                it.copy(
//                    isPlaying = true
//                )
//            }
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