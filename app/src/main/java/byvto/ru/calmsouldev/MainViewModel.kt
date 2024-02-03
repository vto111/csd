package byvto.ru.calmsouldev

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import byvto.ru.calmsouldev.data.local.FilesDatabase
import byvto.ru.calmsouldev.data.local.FilesEntity
import byvto.ru.calmsouldev.model.Files
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
    private val db: FilesDatabase,
    @ApplicationContext context: Context
): ViewModel() {

    val playList = mutableStateOf(listOf<Files>())
//    val playList = mutableStateOf(listOf<FilesEntity>())
//    private val _playList = MutableStateFlow(listOf<FilesEntity>())
//    val playList = _playList.asStateFlow()

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.asStateFlow()

    private val player = ExoPlayer.Builder(context).build()

    init {
        if (!checkDb()) initDb(context = context)
        getAll()
    }

    fun onEvent(event: MainEvent) {
        when(event) {
            is MainEvent.SmallHeadClick -> {
                //TODO запускать плеер с нужным треком
                viewModelScope.launch {
                    getById(event.id)
                }
            }
            MainEvent.BigHeadClick -> {
                //TODO запускать рандомный трек
                val rnd = (1..playList.value.size).random()
                viewModelScope.launch {
                    getById(rnd)
                }
            }
            is MainEvent.ToggleFinished -> {
                toggleFinished(event.id)
            }
            is MainEvent.TogglePlayPause -> {
                playPauseToggle()
            }
        }
    }

    private fun initDb(context: Context) = runBlocking {
        //TODO инит базы на новом устройстве!
        // надо придумать как правильно, тут тупо перебор!!!
        Log.i("initDb", "New Device, creating index!")
        val list = context.assets.list("ogg")
        if (list != null) {
            for (f in 0 .. list.size) {
//                println(list[f])
                db.dao.insertFile(
                    FilesEntity(
                        id = f,
                        fileName = list[f],
                        finished = false
                    )
                )
            }
        }
    }

    private fun checkDb(): Boolean = runBlocking {
        return@runBlocking db.dao.checkNewDevice()
    }

    private fun getAll() {
        viewModelScope.launch {
            playList.value = db.dao.getAll().map { it.toFiles() }
//            Log.i("getAll", playList.value.toString())
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
        println("URI: $uri")
        player.setMediaItem(MediaItem.fromUri(uri))
        player.prepare()
    }

    fun setFinished(id: Int) {
        viewModelScope.launch {
            db.dao.finishedById(id, true)
        }
    }

    private fun toggleFinished(id: Int) {
        // должна вызываться когда трек заканчивается
        viewModelScope.launch {
            val entry = db.dao.getById(id)
            if (entry.finished) {
                db.dao.finishedById(id, false)
            } else {
                db.dao.finishedById(id, true)
            }
            //TODO тут что-то не правильно, надо переделать:
//            getAll()
//            getById(id)
        }
    }

    private fun playPauseToggle() {
        if (player.isPlaying) {
            player.pause()
            _playerState.value = _playerState.value.copy(isPlaying = false)
        } else {
            player.play()
            _playerState.value = _playerState.value.copy(isPlaying = true)
        }
//        if (_playerState.value.isPlaying) {
//            Log.e("PLAYER", "PAUSE pressed")
////            player.release()
//            player.pause()
//            _playerState.value = _playerState.value.copy(isPlaying = false)
//        } else {
//            Log.e("PLAYER", "PLAY pressed")
////            player.setMediaItem(MediaItem.fromUri(uri))
////            player.prepare()
//            player.play()
//            _playerState.value = _playerState.value.copy(isPlaying = true)
//        }
    }
}