package byvto.ru.calmsouldev

import androidx.lifecycle.ViewModel
import byvto.ru.calmsouldev.data.local.FilesDao
import kotlin.random.Random

class MainViewModel(
    private val dao: FilesDao
): ViewModel() {

    val fileList = dao.getAllFiles()

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