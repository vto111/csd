package byvto.ru.calmsouldev

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import byvto.ru.calmsouldev.data.local.FilesEntity
import byvto.ru.calmsouldev.ui.theme.CalmSoulDevTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel
) {

    val playList = viewModel.playList.value
    val currentTrack = viewModel.currentTrack.value

    val scope = rememberCoroutineScope()
    val playerState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed,
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = playerState
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text(text = "Track number: ${currentTrack.id}")
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "FileName: ${currentTrack.fileName}")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = currentTrack.finished,
                        onClick = {
                            viewModel.onEvent(MainEvent.ToggleFinished(currentTrack.id))
                        }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { scope.launch { playerState.collapse() } }
                    ) {
                        Text(text = "Hide")
                    }
                }
            }
        },
        sheetPeekHeight = 10.dp,
    ) {
        Column(
            modifier = Modifier
                .padding(paddingValues = it)
                .fillMaxWidth()
        ) {
            Image(
                modifier = Modifier
//                    .size(256.dp)
                    .fillMaxWidth()
                    .clickable {
                        viewModel.onEvent(MainEvent.BigHeadClick)
                        scope.launch { playerState.expand() }
                    },
                imageVector = ImageVector.vectorResource(id = R.drawable.tollev_white_v2),
                contentDescription = "head",
            )
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier.fillMaxWidth()
                ) {
//                    items(playListFlow.value.size) {
                    items(playList.size) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Image(
                                modifier = Modifier
//                                .size(36.dp)
                                    .clickable {
                                        //TODO запустить воспроизведение
                                        viewModel.onEvent(MainEvent.SmallHeadClick(playList[it].id))
                                        scope.launch { playerState.expand() }
                                    }
                                    .background(
                                        if (currentTrack.id == playList[it].id) Color.LightGray
                                        else MaterialTheme.colors.background
                                    ),
                                imageVector = if (playList[it].finished) {
                                    ImageVector.vectorResource(id = R.drawable.tollev_green_v2)
                                } else {
                                    ImageVector.vectorResource(id = R.drawable.tollev_white_v2)
                                },
                                contentDescription = "item",
                                contentScale = ContentScale.FillWidth
                            )
                            Text(
                                text = playList[it].id.toString(),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                textAlign = TextAlign.End,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}
