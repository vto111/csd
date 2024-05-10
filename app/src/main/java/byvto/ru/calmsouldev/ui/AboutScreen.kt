package byvto.ru.calmsouldev.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import byvto.ru.calmsouldev.menuItems
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavController
) {

    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedMenuIndex by rememberSaveable {
        mutableStateOf(1)
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
                Text(text = "Developer: Sergey Razrabov")
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "E-mail: info@site.com",
                    modifier = Modifier.clickable {
                        context.sendMail(to = "info@site.com", subject = "CalmSoul Application")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Web: www.site.com",
                    modifier = Modifier.clickable {
                        context.openWeb(link = "https://www.site.com")
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

fun Context.sendMail(to: String, subject: String) {
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"  // or "vnd.android.cursor.item/email"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // TODO: Handle case where no email app is available
    } catch (t: Throwable) {
        // TODO: Handle potential other type of exceptions
    }
}

fun Context.openWeb(link: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // TODO: Handle case where no email app is available
    } catch (t: Throwable) {
        // TODO: Handle potential other type of exceptions
    }
}