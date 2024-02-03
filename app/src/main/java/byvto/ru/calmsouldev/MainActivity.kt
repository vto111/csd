package byvto.ru.calmsouldev

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import android.window.SplashScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import byvto.ru.calmsouldev.ui.Player
import byvto.ru.calmsouldev.ui.theme.CalmSoulDevTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalmSoulDevTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "splash_screen") {
                    composable("splash_screen") {
                        SplashScreen(navController = navController)
                    }
                    composable("main_screen") {
                        MainScreen(viewModel = hiltViewModel())
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    val scale = remember {
        Animatable(0f)
    }
    
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 5f,
            animationSpec = tween(
                durationMillis = 500,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        delay(2000L)
        navController.navigate("main_screen")
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.moai),
            contentDescription = "moai",
            modifier = Modifier.scale(scale.value)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel
) {

    val playList = viewModel.playList.value
    val playerState by viewModel.playerState.collectAsState()

    val scope = rememberCoroutineScope()
    val bottomState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed,
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomState
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetContent = {
            Player(
                state = playerState,
                playPauseToggle = { viewModel.onEvent(MainEvent.TogglePlayPause) },
                hideClicked = { scope.launch { bottomState.collapse() } }
            )
        },
        sheetPeekHeight = 10.dp,
    ) {
        Column(
            modifier = Modifier
                .padding(paddingValues = it)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                        scope.launch { bottomState.expand() }
                    },
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(playList.size) {
                    Box(
                        modifier = Modifier
                            .clickable {
                                viewModel.onEvent(MainEvent.SmallHeadClick(playList[it].id))
                                scope.launch { bottomState.expand() }
                            }
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
                            textAlign = TextAlign.Center,
//                                textAlign = TextAlign.End,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}