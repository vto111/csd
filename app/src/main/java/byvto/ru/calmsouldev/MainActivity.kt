package byvto.ru.calmsouldev

import android.annotation.SuppressLint
import android.content.res.Resources
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import byvto.ru.calmsouldev.ui.theme.CalmSoulDevTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalmSoulDevTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    Column {

                        HeadAvatar()
                        MiniHeadColumn()
//                        PlaySampleAudio()
                    }


                }
            }
        }
    }
}

@Composable
fun PlaySampleAudio() {
    val player = MediaPlayer()

}

@Composable
fun HeadAvatar() {
    Image(
        modifier = Modifier.size(256.dp),
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
fun MiniHeadColumn() {
    Column() {
        repeat(7){
            MiniHeadItemRow()
        }
    }
}

@Composable
fun MiniHeadItemRow(){
    Row() {
        repeat(7){
            MiniHeadItem()
        }
    }

}

@Composable
fun MiniHeadItem(){
    Image(
        modifier = Modifier
            .size(36.dp),
        imageVector = ImageVector.vectorResource(id = R.drawable.tollev_white_v2),
        contentDescription = "item",

        )

}