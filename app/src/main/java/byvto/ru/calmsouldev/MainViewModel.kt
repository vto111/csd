package byvto.ru.calmsouldev

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import byvto.ru.calmsouldev.data.local.FilesDatabase
import byvto.ru.calmsouldev.data.local.FilesEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
            println(fileList.value)
        }
    }

    fun initDb() {
        //TODO прочитать имена всех файлов и записать в БД
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



    private fun getRandom(countSoundFiles: Int): Int {
        val randomValues = List(1) { Random.nextInt(0, countSoundFiles) }
        return randomValues[0]
    }

    fun clickHeadAvatar(countSoundFiles: Int) {

        val numberRandom = getRandom(countSoundFiles)
        println("click clickHeadAvatar")
        println(numberRandom)

    }
}