package byvto.ru.calmsouldev

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import byvto.ru.calmsouldev.data.local.FilesDatabase
import byvto.ru.calmsouldev.data.local.FilesEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainViewModel @Inject constructor(
    private val db: FilesDatabase
): ViewModel() {

    var fileList = mutableStateOf(listOf<FilesEntity>())

    init {
        getAll()
    }

    fun getAll() {
        //TODO функция должна возвращать список всех файлов из таблицы
        viewModelScope.launch {
            fileList.value = db.dao.getAll()
//            println(fileList.value)
        }
    }

    fun setFinished(id: Int) {
        // меняет статут по id
        viewModelScope.launch {
            db.dao.finishedById(id, true)
        }
    }

    fun toggleFinished(id: Int) {
        viewModelScope.launch {
            val entry = db.dao.getById(id)
            if (entry.finished) {
                db.dao.finishedById(id, false)
            } else {
                db.dao.finishedById(id, true)
            }
            getAll()
        }
    }

    fun initDb() {
        //TODO прочитать имена всех файлов и записать в БД (делать 1 раз)
        viewModelScope.launch {
            for (i in 0..15) { // надо придумать как правильно, тут тупо перебор
                db.dao.insertFile(
                    FilesEntity(
                        id = i,
                        fileName = "$i.ogg",
                        finished = false
                    )
                )
            }
        }
    }

    fun clickHeadAvatar() {
        val rnd = (1..fileList.value.size).random()
        println(rnd)
    }
}