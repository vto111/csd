package byvto.ru.calmsouldev

import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class) @Composable
fun MainScreen(
    viewModel: MainViewModel
) {

    val playList = viewModel.playList.value
    val playerState by viewModel.playerState.collectAsState()

    Scaffold() {
        Column(
            modifier = Modifier
                .padding(paddingValues = it)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(playList.size) {
                    Box(
                        modifier = Modifier
//                            .clickable {
//                                viewModel.onEvent(MainEvent.SmallHeadClick(playList[it].id))
//                            }
//                                .fillMaxSize()
                            .padding(4.dp),
//                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .background(
                                    if (playerState.id == playList[it].id) Color.LightGray
                                    else MaterialTheme.colors.background
                                ),
                            imageVector = if (playList[it].isFinished) {
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
                            textAlign = TextAlign.Center,
//                                textAlign = TextAlign.End,
                            color = Color.Black
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.tollev_white_v2),
                contentDescription = "head",
                modifier = Modifier
                    .size(300.dp)
//                        .fillMaxWidth()
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .border(4.dp, Color.Gray, CircleShape)
                    .clickable {
                        viewModel.onEvent(MainEvent.BigHeadClick)
                    },
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}