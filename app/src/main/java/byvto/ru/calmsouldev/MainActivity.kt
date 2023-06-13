package byvto.ru.calmsouldev

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import byvto.ru.calmsouldev.ui.theme.CalmSoulDevTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalmSoulDevTheme {
                MainScreen(viewModel = hiltViewModel())
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel
) {

    val fileList = viewModel.fileList.value

//    LaunchedEffect(true) {
//        viewModel.getAll()
//    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        Column {
            Image(
                modifier = Modifier
                    .size(256.dp)
                    .clickable {
                        viewModel.clickHeadAvatar()
                    },
                imageVector = ImageVector.vectorResource(id = R.drawable.tollev_white_v2),
                contentDescription = "head",
            )
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7)
                ) {
                    items(fileList.size) {
//                        println(fileList.value[it])
                        Image(
                            modifier = Modifier
                                .size(36.dp)
                                .clickable {
                                    //TODO запустить воспроизведение
                                           viewModel.toggleFinished(fileList[it].id)
                                },
                            imageVector = if (fileList[it].finished) {
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