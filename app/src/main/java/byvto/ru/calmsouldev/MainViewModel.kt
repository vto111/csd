package byvto.ru.calmsouldev

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.exoplayer.ExoPlayer
import byvto.ru.calmsouldev.data.local.TracksDatabase
import byvto.ru.calmsouldev.data.local.TrackEntity
import byvto.ru.calmsouldev.model.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val db: TracksDatabase,
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
            prepare()
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
        if (!checkDb()) initDb(context = context)
        getAll()
    }

    fun onEvent(event: MainEvent) {
        when(event) {
            is MainEvent.BigHeadClick -> bigHeadClick()
            is MainEvent.ResetClick -> resetFinished()
        }
    }

    private fun initDb(context: Context) = runBlocking {
        //TODO инит базы на новом устройстве!
        // надо придумать как правильно, тут тупо перебор!!!
        // стоит перенести в SplashScreen.
        Log.i("initDb", "New Device, creating index!")
        context.assets.list("ogg")?.forEachIndexed { index, file ->
//            Log.i(index.toString(), file)
            db.dao.insertTrack(
                TrackEntity(
                    id = index,
                    fileName = file,
                    isFinished = false,
                    order = 0
                )
            )
        }
    }

    private fun checkDb(): Boolean = runBlocking {
        return@runBlocking db.dao.checkNewDevice()
    }

    private fun getAll() {
        viewModelScope.launch {
            _playList.value = db.dao.getAll().map { it.toTrack() }
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
                    isPlaying = false
                )
            }
        } else {
            val rnd = playList.value.filter {
                !it.isFinished
            }.map { it.id }.random()
            viewModelScope.launch {
                getById(rnd)
            }
            _playerState.update {
                it.copy(
                    isPlaying = true
                )
            }
        }
    }

    private suspend fun getById(id: Int) {
        val result = db.dao.getById(id)
        _playerState.update {
            it.copy(
                id = result.id,
                fileName = result.fileName
            )
        }
        val uri = Uri.fromFile(File("android_asset/ogg/${playerState.value.fileName}"))
        player.setMediaItem(MediaItem.fromUri(uri))
//        player.prepare()
    }

    private fun setFinished(id: Int) {
        viewModelScope.launch {
            db.dao.setFinished(
                id = id
            )
            _playerState.update {
                it.copy(
                    isPlaying = false
                )
            }
            getAll()    //TODO продумать как обновлять без запроса всего списка
        }
    }

    private fun resetFinished() {
        viewModelScope.launch {
            db.dao.resetFinished()
            _playerState.update {
                it.copy(
                    allDone = false
                )
            }
        }
        getAll()
    }
}