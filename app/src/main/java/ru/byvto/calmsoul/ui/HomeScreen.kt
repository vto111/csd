package ru.byvto.calmsoul.ui

import android.widget.Toast
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.lazy.grid.LazyGridLayoutInfo
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.byvto.calmsoul.MainEvent
import ru.byvto.calmsoul.R
import ru.byvto.calmsoul.menuItems
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {

    val playList by viewModel.playList.collectAsState()
    val playerState by viewModel.playerState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedMenuIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current
    val listState = rememberLazyGridState()

    val currentIndex by viewModel.currentIndex.collectAsState()

    LaunchedEffect(true) {
        viewModel.getTrackList()
        viewModel.channel.collect { event ->
            when (event) {
                is MainEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
                else -> Unit
            }
        }
    }

    LaunchedEffect(currentIndex) {
        listState.animateScrollToItem(currentIndex)
//        listState.animateScrollToItemCenter(currentIndex)
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                menuItems.forEachIndexed { index, menuItem ->
                    NavigationDrawerItem(
                        label = { Text(text = menuItem.title) },
                        selected = index == selectedMenuIndex,
                        onClick = {
                            if (navController.currentDestination?.route != menuItem.route) {
//                            if (navController.currentBackStackEntry?.destination?.route != menuItem.route) {
                                navController.navigate(menuItem.route)
                                selectedMenuIndex = index
                                scope.launch {
                                    drawerState.close()
                                }
                            } else {
                                scope.launch {
                                    drawerState.close()
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedMenuIndex) {
                                    menuItem.selectedIcon
                                } else menuItem.selectedIcon,
                                contentDescription = menuItem.title
                            )
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text =
                                if (playerState.isPlaying) playerState.description else "Calm Soul",
                            modifier = Modifier.basicMarquee()
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "menu"
                            )
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(paddingValues = it)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
//                Spacer(modifier = Modifier.weight(1f))
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .weight(1f)
//                            .fillMaxWidth()
                            .padding(16.dp),
                    )
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp),
                    state = listState
                ) {

                    items(playList.size) { item ->
                        Box(
                            modifier = Modifier
                                .clickable {
                                    if (playList[item].isFinished) {
                                        viewModel.onEvent(MainEvent.SmallHeadClick(playList[item].id))
                                    }
                                }
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .background(
                                        if (playerState.id == playList[item].id) Color.LightGray
                                        else MaterialTheme.colorScheme.background
                                    ),
                                imageVector = if (playList[item].isFinished) {
                                    ImageVector.vectorResource(id = R.drawable.tollev_green_v2)
                                } else {
                                    ImageVector.vectorResource(id = R.drawable.tollev_white_v2)
                                },
                                contentDescription = "item",
                                contentScale = ContentScale.FillWidth
                            )
                        }
                    }

                }

                if (playerState.allDone) {
                    Button(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background),
                        onClick = { viewModel.onEvent(MainEvent.ResetClick) }
                    ) {
                        Text(text = "RESET")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                BigButton(
                    modifier = Modifier.height(200.dp),
                    isPlaying = playerState.isPlaying,
                    onClick = { viewModel.onEvent(MainEvent.BigHeadClick) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun BigButton(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "InfiniteTransition")
    val color by infiniteTransition.animateColor(
        initialValue = Color.White,
        targetValue = Color.DarkGray,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ColorAnimation"
    )
    Image(
        imageVector = ImageVector.vectorResource(id = R.drawable.tollev_white_v3),
        contentDescription = "BigHead",
        modifier = modifier
            .size(200.dp)
            .clip(CircleShape)
            .background(if (isPlaying) color else Color.White)
            .clickable { onClick() },
    )
}

suspend fun LazyGridState.animateScrollToItemCenter(index: Int) {
    layoutInfo.resolveItemOffsetToCenter(index)?.let {
        animateScrollToItem(index, it)
        return
    }

    scrollToItem(index)

    layoutInfo.resolveItemOffsetToCenter(index)?.let {
        animateScrollToItem(index, it)
    }
}

private fun LazyGridLayoutInfo.resolveItemOffsetToCenter(index: Int): Int? {
    val itemInfo = visibleItemsInfo.firstOrNull { it.index == index } ?: return null
    val containerSize = viewportSize.width - beforeContentPadding - afterContentPadding
    return -(containerSize - itemInfo.size.height) / 2
}