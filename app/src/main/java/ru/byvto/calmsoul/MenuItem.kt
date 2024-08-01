package ru.byvto.calmsoul

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

val menuItems = listOf(
    MenuItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        route = "ru.byvto.calmsoul.HomeScreen"
//        route = "home_screen"
    ),
    MenuItem(
        title = "About",
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info,
        route = "ru.byvto.calmsoul.AboutScreen"
//        route = "about_screen"
    ),
//    MenuItem(
//        title = "Update",
//        selectedIcon = Icons.Filled.CheckCircle,
//        unselectedIcon = Icons.Outlined.CheckCircle,
//        route = "update_screen"
//    )
)