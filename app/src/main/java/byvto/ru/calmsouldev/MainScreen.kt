package byvto.ru.calmsouldev

import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@kotlin.OptIn(ExperimentalMaterial3Api::class)
@OptIn(UnstableApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    navController: NavController
) {

    val playList by viewModel.playList.collectAsState()
    val playerState by viewModel.playerState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedMenuIndex by rememberSaveable {
        mutableStateOf(0)
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
                            if (navController.currentBackStackEntry?.destination?.route != menuItem.route) {
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
                    title = { Text(text = "Calm Soul") },
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
                                .clickable {
                                    if (playList[it].isFinished) {
                                        viewModel.onEvent(MainEvent.SmallHeadClick(playList[it].id))
                                    }
                                }
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .background(
                                        if (playerState.id == playList[it].id) Color.LightGray
                                        else MaterialTheme.colorScheme.background
                                    ),
                                imageVector = if (playList[it].isFinished) {
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
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background),
                        onClick = { viewModel.onEvent(MainEvent.ResetClick) }
                    ) {
                        Text(text = "RESET")
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
}