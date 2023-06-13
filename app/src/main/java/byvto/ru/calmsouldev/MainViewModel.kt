package byvto.ru.calmsouldev

import androidx.lifecycle.ViewModel
import byvto.ru.calmsouldev.data.local.FilesDao
import byvto.ru.calmsouldev.data.local.FilesDatabase
import byvto.ru.calmsouldev.domain.model.File
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainViewModel @Inject constructor(
    private val db: FilesDatabase
): ViewModel() {

    // переменнаятолько для примера
    val fileList = listOf(1,2,3,4,5)

    fun getAll() {
        val data = db.dao.getAllFiles()
        //TODO функция должна возвращать список файлов из таблицы
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