package byvto.ru.calmsouldev


import kotlin.random.Random

class RandomNumberFile(listSoundFile: Array<String>?) {
//    private val listSoundFile
    var listtSoundFiles = listSoundFile?.count()?.toString()!!.toInt()
    var countSoundFiles = listSoundFile?.size

    public fun getRandom(): Int {
        val randomValues = List(1) { Random.nextInt(0, listtSoundFiles) }
        return randomValues[0]

    }
    public fun getNumbersForMiniHead(): List<Int> {
        return (1..listtSoundFiles).toList()
    }
    public fun getCountFiles(): Int? {
        return countSoundFiles
    }
}
