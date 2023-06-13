package byvto.ru.calmsouldev

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import byvto.ru.calmsouldev.ui.theme.CalmSoulDevTheme


class MainActivity(var NumbersForMiniHead: Int = 0) : ComponentActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * Работа с базой данных
         * */
        

        //получение списка треков
        val listSoundFile = assets.list("ogg")
        // объект для работы со списком файлов
        val objRandomNumberFile = RandomNumberFile(listSoundFile)
        // присвоение общей переменной количество файлов
        NumbersForMiniHead = objRandomNumberFile.getCountFiles()!!


//        println(objRandomNumberFile.getCountFiles())
//        val countSoundFiles = listSoundFile?.count()?.toString()!!.toInt()
//        println(countSoundFiles)

//        val objMMP = MainMediaPlayer()
//        println(objMMP)
//        println(objMMP.getNumbersSound())

//        println(objMMP)
//        getRandom(countSoundFiles)
        setContent {
            CalmSoulDevTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    Column {
                        HeadAvatar()
                        BoxMiniHeader(NumbersForMiniHead)
                    }
                }
            }
        }
    }
}

@Composable
fun BoxMiniHeader(NumbersForMiniHead: Int) {
    Box(
        modifier = Modifier
            .widthIn(min = 0.dp, max = 250.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(7)
        ) {
            items(NumbersForMiniHead) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    MiniHeadItem(it)
                }
            }
        }
    }
}

@Composable
fun HeadAvatar() {
    Image(
        modifier = Modifier

            .size(256.dp)
            .clickable {
//                clickHeadAvatar()
            },
        imageVector = ImageVector.vectorResource(id = R.drawable.tollev_white_v2),
        contentDescription = "head",
    )
}



//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    CalmSoulDevTheme {
//        HeadAvatar(countSoundFiles)
//    }
//}

@Composable
fun MiniHeadItem(numberMiniHead: Int) {
    Image(
        modifier = Modifier
            .size(36.dp),
        imageVector = ImageVector.vectorResource(id = R.drawable.tollev_white_v2),
        contentDescription = "item",

        )
    println(numberMiniHead)

}

//fun clickHeadAvatar() {
//    val objRandomNumberFile = RandomNumberFile()
//
//    val numberRandom = objRandomNumberFile.getRandom()
//    println("click clickHeadAvatar")
//    println(numberRandom)
//
//}