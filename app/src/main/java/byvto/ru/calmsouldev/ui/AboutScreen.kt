package byvto.ru.calmsouldev.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import byvto.ru.calmsouldev.R
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
//                Text(text = "Developer: Sergey Razrabov")
                Image(
                    bitmap = ImageBitmap.imageResource(R.drawable.pl_and_ari),
                    modifier = Modifier
                        .size(300.dp)
//                        .fillMaxWidth()
//                        .clip(RectangleShape)
//                        .background(Color.LightGray)
//                        .border(4.dp, Color.Gray, RectangleShape)
                    ,
                    contentDescription = "Философия"
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "E-mail: info@byvto.ru",
                    modifier = Modifier.clickable {
                        context.sendMail(to = "info@byvto.ru", subject = "CalmSoul Application")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Web: www.byvto.ru",
                    modifier = Modifier.clickable {
                        context.openWeb(link = "https://www.byvto.ru")
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