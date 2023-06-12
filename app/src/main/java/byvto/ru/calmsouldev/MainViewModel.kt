package byvto.ru.calmsouldev

import androidx.lifecycle.ViewModel
import kotlin.random.Random

class MainViewModel: ViewModel() {

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