package byvto.ru.calmsouldev

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import byvto.ru.calmsouldev.data.local.FilesDatabase
import byvto.ru.calmsouldev.data.local.FilesEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val db: FilesDatabase
): ViewModel() {

    val playList = mutableStateOf(listOf<FilesEntity>())
    val currentTrack = mutableStateOf(FilesEntity(0, "", false))

    init {
        if (!checkDb()) initDb()
        //TODO bug: при первом запуске читается только одна голова...
        getAll()
    }

    fun onEvent(event: MainEvent) {
        when(event) {
            is MainEvent.SmallHeadClick -> {
                //TODO запускать плеер с нужным треком
                getById(event.id)
            }
            MainEvent.BigHeadClick -> {
                //TODO запускать рандомный трек
                val rnd = (1..playList.value.size).random()
                getById(rnd)
            }
            is MainEvent.ToggleFinished -> {
                toggleFinished(event.id)
            }
        }
    }

    private fun checkDb(): Boolean = runBlocking {
        return@runBlocking db.dao.checkNewDevice()
    }

    private fun getAll() {
        viewModelScope.launch {
            playList.value = db.dao.getAll()
//            Log.i("getAll", playList.value.toString())
        }
    }

    private fun getById(id: Int) = runBlocking {
        currentTrack.value = db.dao.getById(id)
    }

    fun setFinished(id: Int) {
        viewModelScope.launch {
            db.dao.finishedById(id, true)
        }
    }

    private fun toggleFinished(id: Int) {
        viewModelScope.launch {
            val entry = db.dao.getById(id)
            if (entry.finished) {
                db.dao.finishedById(id, false)
            } else {
                db.dao.finishedById(id, true)
            }
            //TODO тут что-то не правильно, надо переделать:
            getAll()
            getById(id)
        }
    }

    private fun initDb() = runBlocking {
        //TODO инит базы на новом устройстве!
        // надо придумать как правильно, тут тупо перебор!!!
        Log.i("initDb", "New Device, creating index!")
//        viewModelScope.launch {
            for (i in 0..15) {
                Log.i("initDb id", i.toString())
                db.dao.insertFile(
                    FilesEntity(
                        id = i,
                        fileName = "$i.ogg",
                        finished = false
                    )
                )
            }
//        }
    }
}