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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import byvto.ru.calmsouldev.ui.theme.CalmSoulDevTheme



class MainActivity() : ComponentActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val listSoundFile = assets.list("ogg")
        val countSoundFiles = listSoundFile?.count()?.toString()!!.toInt()
        println(countSoundFiles)
        setContent {
            CalmSoulDevTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    Column {
                        HeadAvatar()
                        BoxMiniHeader(countSoundFiles)
                    }
                }
            }
        }
    }
}

@Composable
fun BoxMiniHeader(countSoundFiles: Int) {
    Box(
        modifier = Modifier
            .widthIn(min = 0.dp, max = 250.dp)
    ) {
        val numbers = (1..countSoundFiles).toList()

        LazyVerticalGrid(
            columns = GridCells.Fixed(7)
        ) {
            var numberMiniHead: Int = 0
            items(numbers.size) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    MiniHeadItem(numberMiniHead)
                }
                numberMiniHead += 1
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
                clickHeadAvatar()
            },
        imageVector = ImageVector.vectorResource(id = R.drawable.tollev_white_v2),
        contentDescription = "head",
    )
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalmSoulDevTheme {
        HeadAvatar()
    }
}

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

fun clickHeadAvatar(){
    println("click clickHeadAvatar")

}