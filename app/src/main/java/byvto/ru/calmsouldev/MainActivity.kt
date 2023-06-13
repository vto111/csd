package byvto.ru.calmsouldev

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import kotlin.random.Random


class MainActivity() : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {

        //TODO надо один раз наполнить каким-то образом базу файлами

        val listSoundFile = assets.list("ogg")
        val countSoundFiles = listSoundFile?.count()?.toString()!!.toInt()
        println(countSoundFiles)

        super.onCreate(savedInstanceState)
        setContent {
            CalmSoulDevTheme {
                MainScreen(viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        Column {
            Image(
                modifier = Modifier

                    .size(256.dp)
                    .clickable {
//                        viewModel.clickHeadAvatar(countSoundFiles)
                    },
                imageVector = ImageVector.vectorResource(id = R.drawable.tollev_white_v2),
                contentDescription = "head",
            )
            Box(
                modifier = Modifier
                    .widthIn(min = 0.dp, max = 250.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7)
                ) {
                    items(viewModel.fileList.size) {
//                        println(viewModel.fileList[it].fileName)
                        Image(
                            modifier = Modifier
                                .size(36.dp)
                                .clickable {
                                    //TODO запустить воспроизведение
                                },
                            imageVector = if (true) {
//                            imageVector = if (viewModel.fileList[it].finished) {
                                ImageVector.vectorResource(id = R.drawable.tollev_green_v2)
                            } else {
                                ImageVector.vectorResource(id = R.drawable.tollev_white_v2)
                            },
                            contentDescription = "item",
                        )
                    }
                }
            }
        }
    }
}