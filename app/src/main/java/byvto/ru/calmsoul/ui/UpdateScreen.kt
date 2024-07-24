package byvto.ru.calmsoul.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import byvto.ru.calmsoul.MainEvent
import byvto.ru.calmsoul.menuItems
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScreen(
    modifier: Modifier = Modifier,
    viewModel: UpdateViewModel,
    navController: NavController
) {

    val remoteList by viewModel.remoteList.collectAsState()
    val localList by viewModel.localList.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedMenuIndex by rememberSaveable {
        mutableStateOf(2)
    }

    val context = LocalContext.current
    LaunchedEffect(true) {
        viewModel.getLists()
        viewModel.channel.collect { event ->
            when (event) {
                is MainEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
                else -> Unit
            }
        }

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
                    title = { Text(text = "Check Updates") },
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
                Text(text = "LOCAL LIST:")
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(localList.size) { item ->
                        Row(modifier = modifier.fillMaxWidth()) {
                            Text(text = localList[item].id.toString())
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = localList[item].fileName)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "REMOTE LIST:")
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(remoteList.size) { item ->
                        Row(modifier = modifier.fillMaxWidth()) {
                            Text(text = remoteList[item].id.toString())
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = remoteList[item].path)
                        }
                    }
                }
            }
        }
    }
}